/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCancelledNotification(@NotNull Object requestId, String reason, Map<String, Object> meta)
    implements
        McpClientNotification,
        McpServerNotification {

    public static final String NAME = "mcp_cancelled_notification";

    private static final ParseField REQUEST_ID_FIELD = new ParseField("requestId");
    private static final ParseField REASON_FIELD = new ParseField("reason");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCancelledNotification, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpCancelledNotification(args[0], (String) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareField(constructorArg(), (p, c) -> switch (p.currentToken()) {
            case VALUE_NUMBER -> p.numberValue();
            case VALUE_STRING -> p.text();
            default -> throw new ParsingException(
                p.getTokenLocation(),
                "Unsupported value type [" + p.currentToken() + "] for field [" + REQUEST_ID_FIELD.getPreferredName() + "]"
            );
        }, REQUEST_ID_FIELD, ObjectParser.ValueType.VALUE);
        PARSER.declareString(optionalConstructorArg(), REASON_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), new ParseField("_meta"));
    }

    public McpCancelledNotification(StreamInput in) throws IOException {
        this(in.readGenericValue(), in.readOptionalString(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeGenericValue(requestId);
        out.writeOptionalString(reason);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(REQUEST_ID_FIELD.getPreferredName(), requestId);
        if (reason != null) {
            builder.field(REASON_FIELD.getPreferredName(), reason);
        }
        if (meta != null) {
            builder.field("_meta", meta);
        }
        builder.endObject();
        return builder;
    }
}
