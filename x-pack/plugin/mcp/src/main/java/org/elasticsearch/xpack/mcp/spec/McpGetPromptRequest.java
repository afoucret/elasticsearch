/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

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

public record McpGetPromptRequest(String name, Map<String, Object> arguments, Map<String, Object> meta) implements McpRequest {

    private static final ParseField NAME = new ParseField("name");
    private static final ParseField ARGUMENTS = new ParseField("arguments");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpGetPromptRequest, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_get_prompt_request",
        args -> new McpGetPromptRequest((String) args[0], (Map<String, Object>) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), NAME);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), ARGUMENTS);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    @Override
    public String getWriteableName() {
        return "mcp_get_prompt_request";
    }

    public McpGetPromptRequest(StreamInput in) throws IOException {
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
        if (name != null) {
            builder.field(NAME.getPreferredName(), name);
        }
        if (arguments != null) {
            builder.field(ARGUMENTS.getPreferredName(), arguments);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
