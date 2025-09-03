/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCreateMessageRequest(
    List<McpSamplingMessage> messages,
    McpModelPreferences modelPreferences,
    String systemPrompt,
    Boolean includeContext,
    Double temperature,
    Integer maxTokens,
    List<String> stopSequences,
    Map<String, Object> metadata,
    Map<String, Object> meta
) implements Writeable, ToXContentObject {

    private static final ParseField MESSAGES = new ParseField("messages");
    private static final ParseField MODEL_PREFERENCES = new ParseField("modelPreferences");
    private static final ParseField SYSTEM_PROMPT = new ParseField("systemPrompt");
    private static final ParseField INCLUDE_CONTEXT = new ParseField("includeContext");
    private static final ParseField TEMPERATURE = new ParseField("temperature");
    private static final ParseField MAX_TOKENS = new ParseField("maxTokens");
    private static final ParseField STOP_SEQUENCES = new ParseField("stopSequences");
    private static final ParseField METADATA = new ParseField("metadata");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCreateMessageRequest, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_create_message_request",
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
        PARSER.declareObjectArray(constructorArg(), (p, c) -> McpSamplingMessage.PARSER.parse(p, null), MESSAGES);
        PARSER.declareObject(optionalConstructorArg(), McpModelPreferences.PARSER, MODEL_PREFERENCES);
        PARSER.declareString(optionalConstructorArg(), SYSTEM_PROMPT);
        PARSER.declareBoolean(optionalConstructorArg(), INCLUDE_CONTEXT);
        PARSER.declareDouble(optionalConstructorArg(), TEMPERATURE);
        PARSER.declareInt(optionalConstructorArg(), MAX_TOKENS);
        PARSER.declareStringArray(optionalConstructorArg(), STOP_SEQUENCES);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), METADATA);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
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
            in.readMap(StreamInput::readString, StreamInput::readGenericValue),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
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
        out.writeMap(metadata, StreamOutput::writeString, StreamOutput::writeGenericValue);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (messages != null) {
            builder.field(MESSAGES.getPreferredName(), messages);
        }
        if (modelPreferences != null) {
            builder.field(MODEL_PREFERENCES.getPreferredName(), modelPreferences);
        }
        if (systemPrompt != null) {
            builder.field(SYSTEM_PROMPT.getPreferredName(), systemPrompt);
        }
        if (includeContext != null) {
            builder.field(INCLUDE_CONTEXT.getPreferredName(), includeContext);
        }
        if (temperature != null) {
            builder.field(TEMPERATURE.getPreferredName(), temperature);
        }
        if (maxTokens != null) {
            builder.field(MAX_TOKENS.getPreferredName(), maxTokens);
        }
        if (stopSequences != null) {
            builder.field(STOP_SEQUENCES.getPreferredName(), stopSequences);
        }
        if (metadata != null) {
            builder.field(METADATA.getPreferredName(), metadata);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
