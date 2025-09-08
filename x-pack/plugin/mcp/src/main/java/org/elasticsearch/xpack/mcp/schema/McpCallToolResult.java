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
import java.util.List;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCallToolResult(
    @NotNull List<McpContent> content,
    Boolean isError,
    Map<String, Object> structuredContent,
    Map<String, Object> meta
) implements McpServerResult {

    public static final String NAME = "mcp_call_tool_result";

    private static final ParseField CONTENT_FIELD = new ParseField("content");
    private static final ParseField IS_ERROR_FIELD = new ParseField("isError");
    private static final ParseField STRUCTURED_CONTENT_FIELD = new ParseField("structuredContent");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCallToolResult, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpCallToolResult(
            (List<McpContent>) args[0],
            (Boolean) args[1],
            (Map<String, Object>) args[2],
            (Map<String, Object>) args[3]
        )
    );

    static {
        PARSER.declareObjectArray(constructorArg(), (p, c) -> McpContent.fromXContent(p), CONTENT_FIELD);
        PARSER.declareBoolean(optionalConstructorArg(), IS_ERROR_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), STRUCTURED_CONTENT_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpCallToolResult(StreamInput in) throws IOException {
        this(
            in.readNamedWriteableCollectionAsList(McpContent.class),
            in.readOptionalBoolean(),
            in.readOptional(StreamInput::readGenericMap),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeNamedWriteableCollection(content);
        out.writeOptionalBoolean(isError);
        out.writeOptional(StreamOutput::writeGenericMap, structuredContent);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(CONTENT_FIELD.getPreferredName(), content);
        if (isError != null) {
            builder.field(IS_ERROR_FIELD.getPreferredName(), isError);
        }
        if (structuredContent != null) {
            builder.field(STRUCTURED_CONTENT_FIELD.getPreferredName(), structuredContent);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
