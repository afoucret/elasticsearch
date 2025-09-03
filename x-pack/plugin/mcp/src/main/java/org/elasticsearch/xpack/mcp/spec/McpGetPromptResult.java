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
import java.util.List;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpGetPromptResult(@NotNull List<McpPromptMessage> messages, String description, Map<String, Object> meta)
    implements
        McpResult {

    private static final ParseField DESCRIPTION_FIELD = new ParseField("description");
    private static final ParseField MESSAGES_FIELD = new ParseField("messages");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpGetPromptResult, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_get_prompt_result",
        args -> new McpGetPromptResult((List<McpPromptMessage>) args[0], (String) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION_FIELD);
        PARSER.declareObjectArray(constructorArg(), (p, c) -> McpPromptMessage.PARSER.parse(p, null), MESSAGES_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return "mcp_get_prompt_result";
    }

    public McpGetPromptResult(StreamInput in) throws IOException {
        this(
            in.readCollectionAsList(McpPromptMessage::new),
            in.readOptionalString(),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(description);
        out.writeCollection(messages);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(MESSAGES_FIELD.getPreferredName(), messages);
        if (description != null) {
            builder.field(DESCRIPTION_FIELD.getPreferredName(), description);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
