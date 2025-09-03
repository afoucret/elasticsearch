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

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;

public record McpResourceLink(@NotNull String uri) implements McpContent {

    private static final ParseField URI = new ParseField("uri");

    public static final ConstructingObjectParser<McpResourceLink, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_resource_link",
        args -> new McpResourceLink((String) args[0])
    );

    static {
        PARSER.declareString(constructorArg(), URI);
    }

    public McpResourceLink(StreamInput in) throws IOException {
        this(in.readString());
    }

    @Override
    public String getWriteableName() {
        return "resource";
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(uri);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(URI.getPreferredName(), uri);
        builder.endObject();
        return builder;
    }
}
