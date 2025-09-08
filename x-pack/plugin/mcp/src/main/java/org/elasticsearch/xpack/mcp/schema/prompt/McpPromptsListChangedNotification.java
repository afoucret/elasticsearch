/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.prompt;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent.Params;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xpack.mcp.schema.core.McpServerNotification;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpPromptsListChangedNotification(Map<String, Object> meta) implements McpServerNotification {

    public static final String NAME = "mcp_prompts_list_changed_notification";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpPromptsListChangedNotification.class,
        McpPromptsListChangedNotification.NAME,
        McpPromptsListChangedNotification::new
    );

    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpPromptsListChangedNotification, Object> PARSER = new ConstructingObjectParser<>(
        "mcp_prompts_list_changed_notification",
        args -> new McpPromptsListChangedNotification((Map<String, Object>) args[0])
    );

    static {
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpPromptsListChangedNotification(StreamInput in) throws IOException {
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
