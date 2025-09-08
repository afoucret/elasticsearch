/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;

public record McpPromptMessage(@NotNull McpRole role, @NotNull McpContent content) implements NamedWriteable, ToXContentObject {

    private static final ParseField ROLE_FIELD = new ParseField("role");
    private static final ParseField CONTENT_FIELD = new ParseField("content");
    public static final String NAME = "mcp_prompt_message";

    public static final ConstructingObjectParser<McpPromptMessage, Object> PARSER = new ConstructingObjectParser<>(
        "mcp_prompt_message",
        args -> new McpPromptMessage((McpRole) args[0], (McpContent) args[1])
    );

    static {
        PARSER.declareString(constructorArg(), McpRole::valueOf, ROLE_FIELD);
        PARSER.declareObject(constructorArg(), (p, c) -> McpContent.fromXContent(p), CONTENT_FIELD);
    }

    public McpPromptMessage(StreamInput in) throws IOException {
        this(in.readEnum(McpRole.class), in.readNamedWriteable(McpContent.class));
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(role);
        out.writeNamedWriteable(content);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (role != null) {
            builder.field(ROLE_FIELD.getPreferredName(), role);
        }
        if (content != null) {
            builder.field(CONTENT_FIELD.getPreferredName(), content);
        }
        builder.endObject();
        return builder;
    }
}
