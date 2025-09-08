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

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpListPromptsResult(@NotNull List<McpPrompt> prompts, String nextCursor, Map<String, Object> meta)
    implements
        McpServerResult {

    public static final String NAME = "mcp_list_prompts_result";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpListPromptsResult.class,
        McpListPromptsResult.NAME,
        McpListPromptsResult::new
    );

    private static final ParseField PROMPTS_FIELD = new ParseField("prompts");
    private static final ParseField NEXT_CURSOR_FIELD = new ParseField("nextCursor");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpListPromptsResult, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpListPromptsResult((List<McpPrompt>) args[0], (String) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareObjectArray(constructorArg(), McpPrompt.PARSER, PROMPTS_FIELD);
        PARSER.declareString(optionalConstructorArg(), NEXT_CURSOR_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpListPromptsResult(StreamInput in) throws IOException {
        this(in.readCollectionAsList(McpPrompt::new), in.readOptionalString(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeCollection(prompts);
        out.writeOptionalString(nextCursor);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(PROMPTS_FIELD.getPreferredName(), prompts);
        if (nextCursor != null) {
            builder.field(NEXT_CURSOR_FIELD.getPreferredName(), nextCursor);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
