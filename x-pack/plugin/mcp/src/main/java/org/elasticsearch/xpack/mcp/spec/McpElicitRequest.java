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
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpElicitRequest(@NotNull String prompt, McpJsonSchema schema, Map<String, Object> meta) implements McpServerRequest {

    public static final String NAME = "mcp_elicit_request";

    private static final ParseField PROMPT_FIELD = new ParseField("prompt");
    private static final ParseField SCHEMA_FIELD = new ParseField("schema");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpElicitRequest, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpElicitRequest((String) args[0], (McpJsonSchema) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), PROMPT_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpJsonSchema.PARSER, SCHEMA_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpElicitRequest(StreamInput in) throws IOException {
        this(in.readString(), in.readOptionalWriteable(McpJsonSchema::new), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(prompt);
        out.writeOptionalWriteable(schema);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject();
        builder.field(PROMPT_FIELD.getPreferredName(), prompt);
        if (schema != null) {
            builder.field(SCHEMA_FIELD.getPreferredName(), schema);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
