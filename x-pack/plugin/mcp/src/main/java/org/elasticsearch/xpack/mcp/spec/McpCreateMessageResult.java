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
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCreateMessageResult(
    @NotNull McpRole role,
    @NotNull McpContent content,
    @NotNull String model,
    McpStopReason stopReason,
    Map<String, Object> meta
) implements McpClientResult {

    public static final String NAME = "mcp_create_message_result";

    private static final ParseField ROLE_FIELD = new ParseField("role");
    private static final ParseField CONTENT_FIELD = new ParseField("content");
    private static final ParseField MODEL_FIELD = new ParseField("model");
    private static final ParseField STOP_REASON_FIELD = new ParseField("stopReason");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCreateMessageResult, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpCreateMessageResult(
            (McpRole) args[0],
            (McpContent) args[1],
            (String) args[2],
            (McpStopReason) args[3],
            (Map<String, Object>) args[4]
        )
    );

    static {
        PARSER.declareString(constructorArg(), McpRole::valueOf, ROLE_FIELD);
        PARSER.declareObject(constructorArg(), (p, c) -> McpContent.fromXContent(p), CONTENT_FIELD);
        PARSER.declareString(constructorArg(), MODEL_FIELD);
        PARSER.declareString(optionalConstructorArg(), McpStopReason::fromString, STOP_REASON_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpCreateMessageResult(StreamInput in) throws IOException {
        this(
            in.readEnum(McpRole.class),
            in.readNamedWriteable(McpContent.class),
            in.readString(),
            in.readOptionalEnum(McpStopReason.class),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(role);
        out.writeNamedWriteable(content);
        out.writeString(model);
        out.writeOptionalEnum(stopReason);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(ROLE_FIELD.getPreferredName(), role);
        builder.field(CONTENT_FIELD.getPreferredName(), content);
        builder.field(MODEL_FIELD.getPreferredName(), model);
        if (stopReason != null) {
            builder.field(STOP_REASON_FIELD.getPreferredName(), stopReason.value());
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
