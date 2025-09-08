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

public record McpLoggingMessageNotification(
    @NotNull McpLoggingLevel level,
    @NotNull Map<String, Object> data,
    String logger,
    Map<String, Object> meta
) implements McpServerNotification {

    public static final String NAME = "mcp_logging_message_notification";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpLoggingMessageNotification.class,
        McpLoggingMessageNotification.NAME,
        McpLoggingMessageNotification::new
    );

    private static final ParseField LEVEL_FIELD = new ParseField("level");
    private static final ParseField DATA_FIELD = new ParseField("data");
    private static final ParseField LOGGER_FIELD = new ParseField("logger");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpLoggingMessageNotification, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpLoggingMessageNotification(
            (McpLoggingLevel) args[0],
            (Map<String, Object>) args[1],
            (String) args[2],
            (Map<String, Object>) args[3]
        )
    );

    static {
        PARSER.declareString(constructorArg(), McpLoggingLevel::fromString, LEVEL_FIELD);
        PARSER.declareObject(constructorArg(), (p, c) -> p.map(), DATA_FIELD);
        PARSER.declareString(optionalConstructorArg(), LOGGER_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpLoggingMessageNotification(StreamInput in) throws IOException {
        this(
            in.readEnum(McpLoggingLevel.class),
            in.readGenericMap(),
            in.readOptionalString(),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(level);
        out.writeGenericMap(data);
        out.writeOptionalString(logger);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(LEVEL_FIELD.getPreferredName(), level.value());
        builder.field(DATA_FIELD.getPreferredName(), data);

        if (logger != null) {
            builder.field(LOGGER_FIELD.getPreferredName(), logger);
        }
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
