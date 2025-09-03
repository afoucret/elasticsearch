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
import java.util.List;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCreateMessageRequest(
    @NotNull List<McpSamplingMessage> messages,
    McpModelPreferences modelPreferences,
    String systemPrompt,
    Boolean includeContext,
    Double temperature,
    Integer maxTokens,
    List<String> stopSequences,
    Map<String, Object> metadata,
    Map<String, Object> meta
) implements McpServerRequest {

    public static final String NAME = "mcp_create_message_request";

    private static final ParseField MESSAGES_FIELD = new ParseField("messages");
    private static final ParseField MODEL_PREFERENCES_FIELD = new ParseField("modelPreferences");
    private static final ParseField SYSTEM_PROMPT_FIELD = new ParseField("systemPrompt");
    private static final ParseField INCLUDE_CONTEXT_FIELD = new ParseField("includeContext");
    private static final ParseField TEMPERATURE_FIELD = new ParseField("temperature");
    private static final ParseField MAX_TOKENS_FIELD = new ParseField("maxTokens");
    private static final ParseField STOP_SEQUENCES_FIELD = new ParseField("stopSequences");
    private static final ParseField METADATA_FIELD = new ParseField("metadata");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCreateMessageRequest, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpCreateMessageRequest(
            (List<McpSamplingMessage>) args[0],
            (McpModelPreferences) args[1],
            (String) args[2],
            (Boolean) args[3],
            (Double) args[4],
            (Integer) args[5],
            (List<String>) args[6],
            (Map<String, Object>) args[7],
            (Map<String, Object>) args[8]
        )
    );

    static {
        PARSER.declareObjectArray(constructorArg(), (p, c) -> McpSamplingMessage.PARSER.parse(p, null), MESSAGES_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpModelPreferences.PARSER, MODEL_PREFERENCES_FIELD);
        PARSER.declareString(optionalConstructorArg(), SYSTEM_PROMPT_FIELD);
        PARSER.declareBoolean(optionalConstructorArg(), INCLUDE_CONTEXT_FIELD);
        PARSER.declareDouble(optionalConstructorArg(), TEMPERATURE_FIELD);
        PARSER.declareInt(optionalConstructorArg(), MAX_TOKENS_FIELD);
        PARSER.declareStringArray(optionalConstructorArg(), STOP_SEQUENCES_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), METADATA_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpCreateMessageRequest(StreamInput in) throws IOException {
        this(
            in.readCollectionAsList(McpSamplingMessage::new),
            in.readOptionalWriteable(McpModelPreferences::new),
            in.readOptionalString(),
            in.readOptionalBoolean(),
            in.readOptionalDouble(),
            in.readOptionalVInt(),
            in.readOptionalStringCollectionAsList(),
            in.readOptional(StreamInput::readGenericMap),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeCollection(messages);
        out.writeOptionalWriteable(modelPreferences);
        out.writeOptionalString(systemPrompt);
        out.writeOptionalBoolean(includeContext);
        out.writeOptionalDouble(temperature);
        out.writeOptionalVInt(maxTokens);
        out.writeOptionalStringCollection(stopSequences);
        out.writeOptional(StreamOutput::writeGenericMap, metadata);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject();
        builder.field(MESSAGES_FIELD.getPreferredName(), messages);
        if (modelPreferences != null) {
            builder.field(MODEL_PREFERENCES_FIELD.getPreferredName(), modelPreferences);
        }
        if (systemPrompt != null) {
            builder.field(SYSTEM_PROMPT_FIELD.getPreferredName(), systemPrompt);
        }
        if (includeContext != null) {
            builder.field(INCLUDE_CONTEXT_FIELD.getPreferredName(), includeContext);
        }
        if (temperature != null) {
            builder.field(TEMPERATURE_FIELD.getPreferredName(), temperature);
        }
        if (maxTokens != null) {
            builder.field(MAX_TOKENS_FIELD.getPreferredName(), maxTokens);
        }
        if (stopSequences != null) {
            builder.field(STOP_SEQUENCES_FIELD.getPreferredName(), stopSequences);
        }
        if (metadata != null) {
            builder.field(METADATA_FIELD.getPreferredName(), metadata);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
