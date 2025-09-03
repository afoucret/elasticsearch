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

public record McpElicitRequest(String prompt, McpJsonSchema schema, Map<String, Object> meta) implements Writeable, ToXContentObject {

    private static final ParseField PROMPT = new ParseField("prompt");
    private static final ParseField SCHEMA = new ParseField("schema");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpElicitRequest, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_elicit_request",
        args -> new McpElicitRequest((String) args[0], (McpJsonSchema) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), PROMPT);
        PARSER.declareObject(optionalConstructorArg(), McpJsonSchema.PARSER, SCHEMA);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    public McpElicitRequest(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readOptionalWriteable(McpJsonSchema::new),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(prompt);
        out.writeOptionalWriteable(schema);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (prompt != null) {
            builder.field(PROMPT.getPreferredName(), prompt);
        }
        if (schema != null) {
            builder.field(SCHEMA.getPreferredName(), schema);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
