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
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

/**
 * Definition for a tool the client can call.
 *
 * @param name         Intended for programmatic or logical use.
 * @param title        Intended for UI and end-user contexts.
 * @param description  A human-readable description of the tool.
 * @param inputSchema  A JSON Schema object defining the expected parameters for the tool.
 * @param outputSchema An optional JSON Schema object defining the structure of the tool's output.
 * @param annotations  Optional additional tool information.
 * @param meta         Additional metadata.
 */
public record McpTool(
    @NotNull String name,
    @NotNull String description,
    String title,
    McpJsonSchema inputSchema,
    Map<String, Object> outputSchema,
    McpToolAnnotations annotations,
    Map<String, Object> meta
) implements NamedWriteable, ToXContentObject {

    public static final String NAME = "mcp_tool";

    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField TITLE_FIELD = new ParseField("title");
    private static final ParseField DESCRIPTION_FIELD = new ParseField("description");
    private static final ParseField INPUT_SCHEMA_FIELD = new ParseField("inputSchema");
    private static final ParseField OUTPUT_SCHEMA_FIELD = new ParseField("outputSchema");
    private static final ParseField ANNOTATIONS_FIELD = new ParseField("annotations");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpTool, Object> PARSER = new ConstructingObjectParser<>(
        "mcp_tool",
        args -> new McpTool(
            (String) args[0],
            (String) args[1],
            (String) args[2],
            (McpJsonSchema) args[3],
            (Map<String, Object>) args[4],
            (McpToolAnnotations) args[5],
            (Map<String, Object>) args[6]
        )
    );

    static {
        PARSER.declareString(constructorArg(), NAME_FIELD);
        PARSER.declareString(constructorArg(), DESCRIPTION_FIELD);
        PARSER.declareString(optionalConstructorArg(), TITLE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpJsonSchema.PARSER, INPUT_SCHEMA_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), OUTPUT_SCHEMA_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpToolAnnotations.PARSER, ANNOTATIONS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpTool(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readString(),
            in.readOptionalString(),
            in.readOptionalNamedWriteable(McpJsonSchema.class),
            in.readOptional(StreamInput::readGenericMap),
            in.readOptionalNamedWriteable(McpToolAnnotations.class),
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
        out.writeString(description);
        out.writeOptionalString(title);
        out.writeOptionalNamedWriteable(inputSchema);
        out.writeOptional(StreamOutput::writeGenericMap, outputSchema);
        out.writeOptionalNamedWriteable(annotations);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(NAME_FIELD.getPreferredName(), name);
        builder.field(DESCRIPTION_FIELD.getPreferredName(), description);
        if (title != null) {
            builder.field(TITLE_FIELD.getPreferredName(), title);
        }
        if (inputSchema != null) {
            builder.field(INPUT_SCHEMA_FIELD.getPreferredName(), inputSchema);
        }
        if (outputSchema != null) {
            builder.field(OUTPUT_SCHEMA_FIELD.getPreferredName(), outputSchema);
        }
        if (annotations != null) {
            builder.field(ANNOTATIONS_FIELD.getPreferredName(), annotations);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
