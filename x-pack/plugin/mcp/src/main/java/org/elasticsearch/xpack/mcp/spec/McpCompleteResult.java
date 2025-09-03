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

public record McpCompleteResult(@NotNull List<String> completions, Map<String, Object> meta) implements McpResult {

    private static final ParseField COMPLETIONS_FIELD = new ParseField("completions");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCompleteResult, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_complete_result",
        args -> new McpCompleteResult((List<String>) args[0], (Map<String, Object>) args[1])
    );

    static {
        PARSER.declareStringArray(constructorArg(), COMPLETIONS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return "mcp_complete_result";
    }

    public McpCompleteResult(StreamInput in) throws IOException {
        this(in.readOptionalStringCollectionAsList(), in.readMap(StreamInput::readString, StreamInput::readGenericValue));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalStringCollection(completions);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(COMPLETIONS_FIELD.getPreferredName(), completions);
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
