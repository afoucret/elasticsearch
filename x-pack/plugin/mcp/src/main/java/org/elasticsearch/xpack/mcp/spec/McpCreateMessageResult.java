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

public record McpCreateMessageResult(@NotNull McpSamplingMessage message, Map<String, Object> meta) implements McpResult {

    private static final ParseField MESSAGE_FIELD = new ParseField("message");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCreateMessageResult, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_create_message_result",
        args -> new McpCreateMessageResult((McpSamplingMessage) args[0], (Map<String, Object>) args[1])
    );

    static {
        PARSER.declareObject(constructorArg(), McpSamplingMessage.PARSER, MESSAGE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return "mcp_create_message_result";
    }

    public McpCreateMessageResult(StreamInput in) throws IOException {
        this(in.readOptionalWriteable(McpSamplingMessage::new), in.readMap(StreamInput::readString, StreamInput::readGenericValue));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalWriteable(message);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(MESSAGE_FIELD.getPreferredName(), message);
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
