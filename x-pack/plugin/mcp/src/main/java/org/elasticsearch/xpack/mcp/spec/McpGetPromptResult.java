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

public record McpGetPromptResult(String description, List<McpPromptMessage> messages, Map<String, Object> meta)
    implements
        Writeable,
        ToXContentObject {

    private static final ParseField DESCRIPTION = new ParseField("description");
    private static final ParseField MESSAGES = new ParseField("messages");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpGetPromptResult, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_get_prompt_result",
        args -> new McpGetPromptResult((String) args[0], (List<McpPromptMessage>) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION);
        PARSER.declareObjectArray(constructorArg(), (p, c) -> McpPromptMessage.PARSER.parse(p, null), MESSAGES);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    public McpGetPromptResult(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readCollectionAsList(McpPromptMessage::new),
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
        if (description != null) {
            builder.field(DESCRIPTION.getPreferredName(), description);
        }
        if (messages != null) {
            builder.field(MESSAGES.getPreferredName(), messages);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
