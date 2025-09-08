/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.*;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpLoggingSetLevelRequest(@NotNull String level, Map<String, Object> meta) implements McpClientRequest {

    public static final String NAME = "mcp_logging_set_level_request";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpLoggingSetLevelRequest.class,
        McpLoggingSetLevelRequest.NAME,
        McpLoggingSetLevelRequest::new
    );

    private static final ParseField LEVEL_FIELD = new ParseField("level");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpLoggingSetLevelRequest, Object> PARSER = new ConstructingObjectParser<>(
        "mcp_logging_set_level_request",
        args -> new McpLoggingSetLevelRequest((String) args[0], (Map<String, Object>) args[1])
    );

    static {
        PARSER.declareString(constructorArg(), LEVEL_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpLoggingSetLevelRequest(StreamInput in) throws IOException {
        this(in.readString(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(level);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(LEVEL_FIELD.getPreferredName(), level);
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
