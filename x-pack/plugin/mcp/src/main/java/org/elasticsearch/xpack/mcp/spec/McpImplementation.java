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
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpImplementation(String name, String title, String version) implements Writeable, ToXContentObject {

    private static final ParseField NAME = new ParseField("name");
    private static final ParseField TITLE = new ParseField("title");
    private static final ParseField VERSION = new ParseField("version");

    public static final ConstructingObjectParser<McpImplementation, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_implementation",
        args -> new McpImplementation((String) args[0], (String) args[1], (String) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), NAME);
        PARSER.declareString(optionalConstructorArg(), TITLE);
        PARSER.declareString(constructorArg(), VERSION);
    }

    public McpImplementation(StreamInput in) throws IOException {
        this(in.readString(), in.readOptionalString(), in.readString());
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        out.writeOptionalString(title);
        out.writeString(version);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (name != null) {
            builder.field(NAME.getPreferredName(), name);
        }
        if (title != null) {
            builder.field(TITLE.getPreferredName(), title);
        }
        if (version != null) {
            builder.field(VERSION.getPreferredName(), version);
        }
        builder.endObject();
        return builder;
    }
}
