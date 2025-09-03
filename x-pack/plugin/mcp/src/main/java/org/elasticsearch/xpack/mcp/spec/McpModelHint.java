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

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;

public record McpModelHint(String name) implements Writeable, ToXContentObject {

    private static final ParseField NAME = new ParseField("name");

    public static final ConstructingObjectParser<McpModelHint, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_model_hint",
        args -> new McpModelHint((String) args[0])
    );

    static {
        PARSER.declareString(constructorArg(), NAME);
    }

    public McpModelHint(StreamInput in) throws IOException {
        this(in.readOptionalString());
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(name);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (name != null) {
            builder.field(NAME.getPreferredName(), name);
        }
        builder.endObject();
        return builder;
    }
}
