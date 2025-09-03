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
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

/**
 * Describes a message issued to or received from an LLM API.
 *
 * @param role    The role of the message sender.
 * @param content The content of the message.
 * @param meta    Additional metadata.
 */
public record McpSamplingMessage(@NotNull McpRole role, @NotNull McpContent content, Map<String, Object> meta)
    implements
        NamedWriteable,
        ToXContentObject {

    private static final ParseField ROLE_FIELD = new ParseField("role");
    private static final ParseField CONTENT_FIELD = new ParseField("content");
    public static final String NAME = "mcp_sampling_message";

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpSamplingMessage, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpSamplingMessage((McpRole) args[0], (McpContent) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), McpRole::valueOf, new ParseField("role"));
        PARSER.declareNamedObject(constructorArg(), (p, c, n) -> p.namedObject(McpContent.class, n, c), new ParseField("content"));
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), new ParseField("_meta"));
    }

    public McpSamplingMessage(StreamInput in) throws IOException {
        this(in.readEnum(McpRole.class), in.readNamedWriteable(McpContent.class), in.readOptional(StreamInput::readGenericMap));
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
