/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

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

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.*;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpJsonSchema(
    String type,
    Map<String, Object> properties,
    List<String> required,
    Boolean additionalProperties,
    Map<String, Object> defs,
    Map<String, Object> definitions
) implements NamedWriteable, ToXContentObject {

    public static final String NAME = "mcp_json_schema";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(McpJsonSchema.class, McpJsonSchema.NAME, McpJsonSchema::new);

    private static final ParseField TYPE_FIELD = new ParseField("type");
    private static final ParseField PROPERTIES_FIELD = new ParseField("properties");
    private static final ParseField REQUIRED_FIELD = new ParseField("required");
    private static final ParseField ADDITIONAL_PROPERTIES_FIELD = new ParseField("additionalProperties");
    private static final ParseField DEFS_FIELD = new ParseField("$defs");
    private static final ParseField DEFINITIONS_FIELD = new ParseField("definitions");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpJsonSchema, Object> PARSER = new ConstructingObjectParser<>(
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
        PARSER.declareString(optionalConstructorArg(), TYPE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), PROPERTIES_FIELD);
        PARSER.declareStringArray(optionalConstructorArg(), REQUIRED_FIELD);
        PARSER.declareBoolean(optionalConstructorArg(), ADDITIONAL_PROPERTIES_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), DEFS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), DEFINITIONS_FIELD);
    }

    public McpJsonSchema(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readOptional(StreamInput::readGenericMap),
            in.readOptionalStringCollectionAsList(),
            in.readOptionalBoolean(),
            in.readOptional(StreamInput::readGenericMap),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(type);
        out.writeOptional(StreamOutput::writeGenericMap, properties);
        out.writeOptionalStringCollection(required);
        out.writeOptionalBoolean(additionalProperties);
        out.writeOptional(StreamOutput::writeGenericMap, defs);
        out.writeOptional(StreamOutput::writeGenericMap, definitions);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (type != null) {
            builder.field(TYPE_FIELD.getPreferredName(), type);
        }
        if (properties != null) {
            builder.field(PROPERTIES_FIELD.getPreferredName(), properties);
        }
        if (required != null) {
            builder.field(REQUIRED_FIELD.getPreferredName(), required);
        }
        if (additionalProperties != null) {
            builder.field(ADDITIONAL_PROPERTIES_FIELD.getPreferredName(), additionalProperties);
        }
        if (defs != null) {
            builder.field(DEFS_FIELD.getPreferredName(), defs);
        }
        if (definitions != null) {
            builder.field(DEFINITIONS_FIELD.getPreferredName(), definitions);
        }
        builder.endObject();
        return builder;
    }
}
