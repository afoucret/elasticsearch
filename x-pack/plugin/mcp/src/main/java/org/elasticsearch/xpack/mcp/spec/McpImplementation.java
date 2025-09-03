/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpImplementation(@NotNull String name, @NotNull String version, String title) implements NamedWriteable, ToXContentObject {

    public static final String NAME = "mcp_implementation";
    public static final ConstructingObjectParser<McpImplementation, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpImplementation((String) args[0], (String) args[1], (String) args[2])
    );
    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField TITLE_FIELD = new ParseField("title");
    private static final ParseField VERSION_FIELD = new ParseField("version");

    static {
        PARSER.declareString(constructorArg(), NAME_FIELD);
        PARSER.declareString(constructorArg(), VERSION_FIELD);
        PARSER.declareString(optionalConstructorArg(), TITLE_FIELD);
    }

    public McpImplementation(StreamInput in) throws IOException {
        this(in.readString(), in.readOptionalString(), in.readString());
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        out.writeString(version);
        out.writeOptionalString(title);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(NAME_FIELD.getPreferredName(), name);
        builder.field(VERSION_FIELD.getPreferredName(), version);
        if (title != null) {
            builder.field(TITLE_FIELD.getPreferredName(), title);
        }
        builder.endObject();
        return builder;
    }
}
