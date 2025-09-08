/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

/**
 * A notification from the client to the server, informing it that the list of roots has changed.
 *
 * @param meta Additional metadata.
 */
public record McpRootsListChangedNotification(Map<String, Object> meta) implements McpClientNotification {

    public static final String NAME = "roots_list_changed";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpRootsListChangedNotification.class,
        McpRootsListChangedNotification.NAME,
        McpRootsListChangedNotification::new
    );

    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpRootsListChangedNotification, Object> PARSER = new ConstructingObjectParser<>(
        "mcp_roots_list_changed_notification",
        args -> new McpRootsListChangedNotification((Map<String, Object>) args[0])
    );

    static {
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpRootsListChangedNotification(StreamInput in) throws IOException {
        this(in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }
}
