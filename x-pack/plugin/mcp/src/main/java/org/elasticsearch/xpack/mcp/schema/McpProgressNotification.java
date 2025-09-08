/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

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

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.*;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpProgressNotification(
    @NotNull Object progressToken,
    double progress,
    Double total,
    String message,
    Map<String, Object> meta
) implements McpClientNotification, McpServerNotification {

    public static final String NAME = "mcp_progress_notification";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpProgressNotification.class,
        McpProgressNotification.NAME,
        McpProgressNotification::new
    );

    private static final ParseField PROGRESS_TOKEN_FIELD = new ParseField("progressToken");
    private static final ParseField PROGRESS_FIELD = new ParseField("progress");
    private static final ParseField TOTAL_FIELD = new ParseField("total");
    private static final ParseField MESSAGE_FIELD = new ParseField("message");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpProgressNotification, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpProgressNotification(args[0], (double) args[1], (Double) args[2], (String) args[3], (Map<String, Object>) args[4])
    );

    static {
        PARSER.declareField(constructorArg(), (p, c) -> switch (p.currentToken()) {
            case VALUE_NUMBER -> p.numberValue();
            case VALUE_STRING -> p.text();
            default -> throw new ParsingException(
                p.getTokenLocation(),
                "Unsupported value type [" + p.currentToken() + "] for field [" + PROGRESS_TOKEN_FIELD.getPreferredName() + "]"
            );
        }, PROGRESS_TOKEN_FIELD, ObjectParser.ValueType.VALUE);
        PARSER.declareDouble(constructorArg(), PROGRESS_FIELD);
        PARSER.declareDouble(optionalConstructorArg(), TOTAL_FIELD);
        PARSER.declareString(optionalConstructorArg(), MESSAGE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpProgressNotification(StreamInput in) throws IOException {
        this(
            in.readGenericValue(),
            in.readDouble(),
            in.readOptionalDouble(),
            in.readOptionalString(),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeGenericValue(progressToken);
        out.writeDouble(progress);
        out.writeOptionalDouble(total);
        out.writeOptionalString(message);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(PROGRESS_TOKEN_FIELD.getPreferredName(), progressToken);
        builder.field(PROGRESS_FIELD.getPreferredName(), progress);
        if (total != null) {
            builder.field(TOTAL_FIELD.getPreferredName(), total);
        }
        if (message != null) {
            builder.field(MESSAGE_FIELD.getPreferredName(), message);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
