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
import java.util.List;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpPrompt(String name, String title, String description, List<McpPromptArgument> arguments, Map<String, Object> meta)
    implements
        Writeable,
        ToXContentObject {

    private static final ParseField NAME = new ParseField("name");
    private static final ParseField TITLE = new ParseField("title");
    private static final ParseField DESCRIPTION = new ParseField("description");
    private static final ParseField ARGUMENTS = new ParseField("arguments");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpPrompt, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_prompt",
        args -> new McpPrompt(
            (String) args[0],
            (String) args[1],
            (String) args[2],
            (List<McpPromptArgument>) args[3],
            (Map<String, Object>) args[4]
        )
    );

    static {
        PARSER.declareString(constructorArg(), NAME);
        PARSER.declareString(optionalConstructorArg(), TITLE);
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION);
        PARSER.declareObjectArray(optionalConstructorArg(), (p, c) -> McpPromptArgument.PARSER.parse(p, null), ARGUMENTS);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    public McpPrompt(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readCollectionAsList(McpPromptArgument::new),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        out.writeOptionalString(title);
        out.writeOptionalString(description);
        out.writeCollection(arguments);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
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
        if (arguments != null) {
            builder.field(ARGUMENTS.getPreferredName(), arguments);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
