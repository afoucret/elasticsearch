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

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;

public record McpImageContent(String url) implements McpContent {

    private static final ParseField URL = new ParseField("url");

    public static final ConstructingObjectParser<McpImageContent, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_image_content",
        args -> new McpImageContent((String) args[0])
    );

    static {
        PARSER.declareString(constructorArg(), URL);
    }

    public McpImageContent(StreamInput in) throws IOException {
        this(in.readString());
    }

    @Override
    public String getWriteableName() {
        return "image";
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(url);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(URL.getPreferredName(), url);
        builder.endObject();
        return builder;
    }
}
