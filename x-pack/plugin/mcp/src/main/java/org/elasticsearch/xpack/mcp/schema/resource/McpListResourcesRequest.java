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

public record McpListResourcesRequest(String cursor, Map<String, Object> meta) implements McpClientRequest {

    public static final String NAME = "mcp_list_resources_request";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpListResourcesRequest.class,
        McpListResourcesRequest.NAME,
        McpListResourcesRequest::new
    );

    private static final ParseField CURSOR_FIELD = new ParseField("cursor");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpListResourcesRequest, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpListResourcesRequest((String) args[0], (Map<String, Object>) args[1])
    );

    static {
        PARSER.declareString(optionalConstructorArg(), CURSOR_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpListResourcesRequest(StreamInput in) throws IOException {
        this(in.readOptionalString(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(cursor);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (cursor != null) {
            builder.field(CURSOR_FIELD.getPreferredName(), cursor);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
