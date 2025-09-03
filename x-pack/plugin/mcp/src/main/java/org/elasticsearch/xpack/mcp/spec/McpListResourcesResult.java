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

public record McpListResourcesResult(List<McpResource> resources, String nextCursor, Map<String, Object> meta) implements McpResult {

    private static final ParseField RESOURCES = new ParseField("resources");
    private static final ParseField NEXT_CURSOR = new ParseField("nextCursor");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpListResourcesResult, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_list_resources_result",
        args -> new McpListResourcesResult((List<McpResource>) args[0], (String) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareObjectArray(constructorArg(), (p, c) -> McpResource.PARSER.parse(p, null), RESOURCES);
        PARSER.declareString(optionalConstructorArg(), NEXT_CURSOR);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    @Override
    public String getWriteableName() {
        return "mcp_list_resources_result";
    }

    public McpListResourcesResult(StreamInput in) throws IOException {
        this(
            in.readCollectionAsList(McpResource::new),
            in.readOptionalString(),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeCollection(resources);
        out.writeOptionalString(nextCursor);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (resources != null) {
            builder.field(RESOURCES.getPreferredName(), resources);
        }
        if (nextCursor != null) {
            builder.field(NEXT_CURSOR.getPreferredName(), nextCursor);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
