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

public record McpReadResourceResult(List<McpResourceContent> contents, Map<String, Object> meta) implements McpResult {

    private static final ParseField CONTENTS = new ParseField("contents");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpReadResourceResult, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_read_resource_result",
        args -> new McpReadResourceResult((List<McpResourceContent>) args[0], (Map<String, Object>) args[1])
    );

    static {
        PARSER.declareNamedObjects(constructorArg(), (p, c, n) -> p.namedObject(McpResourceContent.class, n, c), CONTENTS);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    @Override
    public String getWriteableName() {
        return "mcp_read_resource_result";
    }

    public McpReadResourceResult(StreamInput in) throws IOException {
        this(
            in.readNamedWriteableCollectionAsList(McpResourceContent.class),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeNamedWriteableCollection(contents);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (contents != null) {
            builder.field(CONTENTS.getPreferredName(), contents);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
