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
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpTool(
    String name,
    String title,
    String description,
    McpJsonSchema inputSchema,
    Map<String, Object> outputSchema,
    McpToolAnnotations annotations,
    Map<String, Object> meta
) implements Writeable, ToXContentObject {

    private static final ParseField NAME = new ParseField("name");
    private static final ParseField TITLE = new ParseField("title");
    private static final ParseField DESCRIPTION = new ParseField("description");
    private static final ParseField INPUT_SCHEMA = new ParseField("inputSchema");
    private static final ParseField OUTPUT_SCHEMA = new ParseField("outputSchema");
    private static final ParseField ANNOTATIONS = new ParseField("annotations");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpTool, Void> PARSER = new ConstructingObjectParser<>(
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
        PARSER.declareString(constructorArg(), NAME);
        PARSER.declareString(optionalConstructorArg(), TITLE);
        PARSER.declareString(constructorArg(), DESCRIPTION);
        PARSER.declareObject(optionalConstructorArg(), McpJsonSchema.PARSER, INPUT_SCHEMA);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), OUTPUT_SCHEMA);
        PARSER.declareObject(optionalConstructorArg(), McpToolAnnotations.PARSER, ANNOTATIONS);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    public McpTool(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalWriteable(McpJsonSchema::new),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue),
            in.readOptionalWriteable(McpToolAnnotations::new),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(name);
        out.writeOptionalString(title);
        out.writeOptionalString(description);
        out.writeOptionalWriteable(inputSchema);
        out.writeMap(outputSchema, StreamOutput::writeString, StreamOutput::writeGenericValue);
        out.writeOptionalWriteable(annotations);
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
        if (inputSchema != null) {
            builder.field(INPUT_SCHEMA.getPreferredName(), inputSchema);
        }
        if (outputSchema != null) {
            builder.field(OUTPUT_SCHEMA.getPreferredName(), outputSchema);
        }
        if (annotations != null) {
            builder.field(ANNOTATIONS.getPreferredName(), annotations);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
