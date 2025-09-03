/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCallToolRequest(@NotNull String name, Map<String, Object> arguments, Map<String, Object> meta)
    implements
        McpRequest {

    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField ARGUMENTS_FIELD = new ParseField("arguments");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCallToolRequest, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_call_tool_request",
        args -> new McpCallToolRequest((String) args[0], (Map<String, Object>) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), NAME_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), ARGUMENTS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return "mcp_call_tool_request";
    }

    public McpCallToolRequest(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(name);
        out.writeMap(arguments, StreamOutput::writeString, StreamOutput::writeGenericValue);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject();
        builder.field(NAME_FIELD.getPreferredName(), name);
        if (arguments != null) {
            builder.field(ARGUMENTS_FIELD.getPreferredName(), arguments);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
