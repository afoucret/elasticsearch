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
import java.util.List;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpPrompt(@NotNull String name, String title, String description, List<McpPromptArgument> arguments, Map<String, Object> meta)
    implements
        NamedWriteable,
        ToXContentObject {

    public static final String NAME = "mcp_prompt";

    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField TITLE_FIELD = new ParseField("title");
    private static final ParseField DESCRIPTION_FIELD = new ParseField("description");
    private static final ParseField ARGUMENTS_FIELD = new ParseField("arguments");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpPrompt, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpPrompt(
            (String) args[0],
            (String) args[1],
            (String) args[2],
            (List<McpPromptArgument>) args[3],
            (Map<String, Object>) args[4]
        )
    );

    static {
        PARSER.declareString(constructorArg(), NAME_FIELD);
        PARSER.declareString(optionalConstructorArg(), TITLE_FIELD);
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION_FIELD);
        PARSER.declareObjectArray(optionalConstructorArg(), (p, c) -> McpPromptArgument.PARSER.parse(p, null), ARGUMENTS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpPrompt(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalCollectionAsList(McpPromptArgument::new),
            in.readOptional(StreamInput::readGenericMap)
        );
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
        out.writeOptionalCollection(arguments);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
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
        if (arguments != null) {
            builder.field(ARGUMENTS_FIELD.getPreferredName(), arguments);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
