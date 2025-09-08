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
 * Represents a root directory or file that the server can operate on.
 *
 * @param uri  The URI identifying the root.
 * @param name An optional name for the root.
 * @param meta Additional metadata.
 */
public record McpRoot(@NotNull String uri, String name, Map<String, Object> meta) implements NamedWriteable, ToXContentObject {

    public static final String NAME = "mcp_root";

    private static final ParseField URI_FIELD = new ParseField("uri");
    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpRoot, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpRoot((String) args[0], (String) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), URI_FIELD);
        PARSER.declareString(optionalConstructorArg(), NAME_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpRoot(StreamInput in) throws IOException {
        this(in.readString(), in.readOptionalString(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(uri);
        out.writeOptionalString(name);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(URI_FIELD.getPreferredName(), uri);
        if (name != null) {
            builder.field(NAME_FIELD.getPreferredName(), name);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
