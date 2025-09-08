/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpPromptArgument(@NotNull String name, String title, String description, Boolean required)
    implements
        NamedWriteable,
        ToXContentObject {

    public static final String NAME = "mcp_prompt_argument";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(McpPromptArgument.class, McpPromptArgument.NAME, McpPromptArgument::new);

    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField TITLE_FIELD = new ParseField("title");
    private static final ParseField DESCRIPTION_FIELD = new ParseField("description");
    private static final ParseField REQUIRED_FIELD = new ParseField("required");

    public static final ConstructingObjectParser<McpPromptArgument, Object> PARSER = new ConstructingObjectParser<>(
        "mcp_prompt_argument",
        args -> new McpPromptArgument((String) args[0], (String) args[1], (String) args[2], (Boolean) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), NAME_FIELD);
        PARSER.declareString(optionalConstructorArg(), TITLE_FIELD);
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION_FIELD);
        PARSER.declareBoolean(optionalConstructorArg(), REQUIRED_FIELD);
    }

    public McpPromptArgument(StreamInput in) throws IOException {
        this(in.readString(), in.readOptionalString(), in.readOptionalString(), in.readOptionalBoolean());
    }

    @Override
    public String getWriteableName() {
        return NAME;
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
            builder.field(NAME_FIELD.getPreferredName(), name);
        }
        if (title != null) {
            builder.field(TITLE_FIELD.getPreferredName(), title);
        }
        if (description != null) {
            builder.field(DESCRIPTION_FIELD.getPreferredName(), description);
        }

        if (required != null) {
            builder.field(REQUIRED_FIELD.getPreferredName(), required);
        }

        builder.endObject();
        return builder;
    }
}
