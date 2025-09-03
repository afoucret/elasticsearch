/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

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

public record McpPromptArgument(String name, String title, String description, Boolean required)
    implements
        NamedWriteable,
        ToXContentObject {

    private static final ParseField NAME = new ParseField("name");
    private static final ParseField TITLE = new ParseField("title");
    private static final ParseField DESCRIPTION = new ParseField("description");
    private static final ParseField REQUIRED = new ParseField("required");
    public static final String NAME_FIELD = "mcp_prompt_argument";

    public static final ConstructingObjectParser<McpPromptArgument, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_prompt_argument",
        args -> new McpPromptArgument((String) args[0], (String) args[1], (String) args[2], (Boolean) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), NAME);
        PARSER.declareString(optionalConstructorArg(), TITLE);
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION);
        PARSER.declareBoolean(optionalConstructorArg(), REQUIRED);
    }

    public McpPromptArgument(StreamInput in) throws IOException {
        this(in.readString(), in.readOptionalString(), in.readOptionalString(), in.readOptionalBoolean());
    }

    @Override
    public String getWriteableName() {
        return NAME_FIELD;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        out.writeOptionalString(title);
        out.writeOptionalString(description);
        out.writeOptionalBoolean(required);
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
        if (description != null) {
            builder.field(DESCRIPTION.getPreferredName(), description);
        }
        if (required != null) {
            builder.field(REQUIRED.getPreferredName(), required);
        }
        builder.endObject();
        return builder;
    }
}
