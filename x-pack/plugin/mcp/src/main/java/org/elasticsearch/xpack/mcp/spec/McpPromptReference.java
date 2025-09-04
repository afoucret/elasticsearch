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
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpPromptReference(@NotNull String name, String title) implements McpReference {

    public static final String NAME = "mcp_prompt_reference";
    public static final String TYPE_VALUE = "ref/prompt";

    private static final ParseField TYPE_FIELD = new ParseField("type");
    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField TITLE_FIELD = new ParseField("title");

    public static final ConstructingObjectParser<McpPromptReference, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpPromptReference((String) args[0], (String) args[1])
    );

    static {
        PARSER.declareString(constructorArg(), NAME_FIELD);
        PARSER.declareString(optionalConstructorArg(), TITLE_FIELD);
        PARSER.declareString(optionalConstructorArg(), TYPE_FIELD);
    }

    public McpPromptReference(StreamInput in) throws IOException {
        this(in.readString(), in.readOptionalString());
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public Type type() {
        return Type.PROMPT;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(type());
        out.writeString(name);
        out.writeOptionalString(title);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(TYPE_FIELD.getPreferredName(), TYPE_VALUE);
        builder.field(NAME_FIELD.getPreferredName(), name);
        if (title != null) {
            builder.field(TITLE_FIELD.getPreferredName(), title);
        }
        builder.endObject();
        return builder;
    }
}
