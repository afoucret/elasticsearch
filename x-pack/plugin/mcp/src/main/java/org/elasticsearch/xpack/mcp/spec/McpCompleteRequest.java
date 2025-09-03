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
import java.util.List;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCompleteRequest(
    String prompt,
    Map<String, Object> promptArguments,
    List<McpPromptMessage> messages,
    McpModelPreferences modelPreferences,
    Map<String, Object> meta
) implements McpClientRequest {

    public static final String NAME = "mcp_complete_request";

    private static final ParseField PROMPT_FIELD = new ParseField("prompt");
    private static final ParseField PROMPT_ARGUMENTS_FIELD = new ParseField("prompt_arguments");
    private static final ParseField MESSAGES_FIELD = new ParseField("messages");
    private static final ParseField MODEL_PREFERENCES_FIELD = new ParseField("model_preferences");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCompleteRequest, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpCompleteRequest(
            (String) args[0],
            (Map<String, Object>) args[1],
            (List<McpPromptMessage>) args[2],
            (McpModelPreferences) args[3],
            (Map<String, Object>) args[4]
        )
    );

    static {
        PARSER.declareString(optionalConstructorArg(), PROMPT_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), PROMPT_ARGUMENTS_FIELD);
        PARSER.declareObjectArray(optionalConstructorArg(), (p, c) -> McpPromptMessage.PARSER.parse(p, null), MESSAGES_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> McpModelPreferences.PARSER.parse(p, null), MODEL_PREFERENCES_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpCompleteRequest(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readOptional(StreamInput::readGenericMap),
            in.readOptionalCollectionAsList(McpPromptMessage::new),
            in.readOptionalWriteable(McpModelPreferences::new),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(prompt);
        out.writeOptional(StreamOutput::writeGenericMap, promptArguments);
        out.writeOptionalCollection(messages);
        out.writeOptionalWriteable(modelPreferences);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject();
        if (prompt != null) {
            builder.field(PROMPT_FIELD.getPreferredName(), prompt);
        }
        if (promptArguments != null) {
            builder.field(PROMPT_ARGUMENTS_FIELD.getPreferredName(), promptArguments);
        }
        if (messages != null) {
            builder.field(MESSAGES_FIELD.getPreferredName(), messages);
        }
        if (modelPreferences != null) {
            builder.field(MODEL_PREFERENCES_FIELD.getPreferredName(), modelPreferences);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
