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
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCallToolResult(List<McpContent> content, Boolean isError, Map<String, Object> structuredContent, Map<String, Object> meta)
    implements
        McpResult {

    private static final ParseField CONTENT = new ParseField("content");
    private static final ParseField IS_ERROR = new ParseField("isError");
    private static final ParseField STRUCTURED_CONTENT = new ParseField("structuredContent");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCallToolResult, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_call_tool_result",
        args -> new McpCallToolResult(
            (List<McpContent>) args[0],
            (Boolean) args[1],
            (Map<String, Object>) args[2],
            (Map<String, Object>) args[3]
        )
    );

    static {
        PARSER.declareNamedObjects(constructorArg(), (p, c, n) -> p.namedObject(McpContent.class, n, c), CONTENT);
        PARSER.declareBoolean(optionalConstructorArg(), IS_ERROR);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), STRUCTURED_CONTENT);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    @Override
    public String getWriteableName() {
        return "mcp_call_tool_result";
    }

    public McpCallToolResult(StreamInput in) throws IOException {
        this(
            in.readNamedWriteableCollectionAsList(McpContent.class),
            in.readOptionalBoolean(),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeCollection(content);
        out.writeOptionalBoolean(isError);
        out.writeMap(structuredContent, StreamOutput::writeString, StreamOutput::writeGenericValue);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (content != null) {
            builder.field(CONTENT.getPreferredName(), content);
        }
        if (isError != null) {
            builder.field(IS_ERROR.getPreferredName(), isError);
        }
        if (structuredContent != null) {
            builder.field(STRUCTURED_CONTENT.getPreferredName(), structuredContent);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
