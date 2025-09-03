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

import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpJsonSchema(
    String type,
    Map<String, Object> properties,
    List<String> required,
    Boolean additionalProperties,
    Map<String, Object> defs,
    Map<String, Object> definitions
) implements Writeable, ToXContentObject {

    private static final ParseField TYPE = new ParseField("type");
    private static final ParseField PROPERTIES = new ParseField("properties");
    private static final ParseField REQUIRED = new ParseField("required");
    private static final ParseField ADDITIONAL_PROPERTIES = new ParseField("additionalProperties");
    private static final ParseField DEFS = new ParseField("$defs");
    private static final ParseField DEFINITIONS = new ParseField("definitions");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpJsonSchema, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_json_schema",
        args -> new McpJsonSchema(
            (String) args[0],
            (Map<String, Object>) args[1],
            (List<String>) args[2],
            (Boolean) args[3],
            (Map<String, Object>) args[4],
            (Map<String, Object>) args[5]
        )
    );

    static {
        PARSER.declareString(optionalConstructorArg(), TYPE);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), PROPERTIES);
        PARSER.declareStringArray(optionalConstructorArg(), REQUIRED);
        PARSER.declareBoolean(optionalConstructorArg(), ADDITIONAL_PROPERTIES);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), DEFS);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), DEFINITIONS);
    }

    public McpJsonSchema(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue),
            in.readOptionalStringCollectionAsList(),
            in.readOptionalBoolean(),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(type);
        out.writeMap(properties, StreamOutput::writeString, StreamOutput::writeGenericValue);
        out.writeOptionalStringCollection(required);
        out.writeOptionalBoolean(additionalProperties);
        out.writeMap(defs, StreamOutput::writeString, StreamOutput::writeGenericValue);
        out.writeMap(definitions, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (type != null) {
            builder.field(TYPE.getPreferredName(), type);
        }
        if (properties != null) {
            builder.field(PROPERTIES.getPreferredName(), properties);
        }
        if (required != null) {
            builder.field(REQUIRED.getPreferredName(), required);
        }
        if (additionalProperties != null) {
            builder.field(ADDITIONAL_PROPERTIES.getPreferredName(), additionalProperties);
        }
        if (defs != null) {
            builder.field(DEFS.getPreferredName(), defs);
        }
        if (definitions != null) {
            builder.field(DEFINITIONS.getPreferredName(), definitions);
        }
        builder.endObject();
        return builder;
    }
}
