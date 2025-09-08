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
 * A known resource that the server is capable of reading.
 *
 * @param name        Intended for programmatic or logical use.
 * @param title       Intended for UI and end-user contexts.
 * @param uri         The URI of this resource.
 * @param description A description of what this resource represents.
 * @param mimeType    The MIME type of this resource, if known.
 * @param annotations Optional annotations for the client.
 * @param size        The size of the raw resource content, in bytes, if known.
 * @param meta        Additional metadata.
 */
public record McpResource(
    @NotNull String uri,
    @NotNull String name,
    String title,
    String description,
    String mimeType,
    Long size,
    McpAnnotations annotations,
    Map<String, Object> meta
) implements NamedWriteable, ToXContentObject {

    public static final String NAME = "mcp_resource";

    private static final ParseField URI_FIELD = new ParseField("uri");
    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField TITLE_FIELD = new ParseField("title");
    private static final ParseField DESCRIPTION_FIELD = new ParseField("description");
    private static final ParseField MIME_TYPE_FIELD = new ParseField("mimeType");
    private static final ParseField SIZE_FIELD = new ParseField("size");
    private static final ParseField ANNOTATIONS_FIELD = new ParseField("annotations");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpResource, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpResource(
            (String) args[0],
            (String) args[1],
            (String) args[2],
            (String) args[3],
            (String) args[4],
            (Long) args[5],
            (McpAnnotations) args[6],
            (Map<String, Object>) args[7]
        )
    );

    static {
        PARSER.declareString(constructorArg(), URI_FIELD);
        PARSER.declareString(constructorArg(), NAME_FIELD);
        PARSER.declareString(optionalConstructorArg(), TITLE_FIELD);
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION_FIELD);
        PARSER.declareString(optionalConstructorArg(), MIME_TYPE_FIELD);
        PARSER.declareLong(optionalConstructorArg(), SIZE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpAnnotations.PARSER, ANNOTATIONS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpResource(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalLong(),
            in.readOptionalNamedWriteable(McpAnnotations.class),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(uri);
        out.writeString(name);
        out.writeOptionalString(title);
        out.writeOptionalString(description);
        out.writeOptionalString(mimeType);
        out.writeOptionalLong(size);
        out.writeOptionalNamedWriteable(annotations);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (uri != null) {
            builder.field(URI_FIELD.getPreferredName(), uri);
        }
        if (name != null) {
            builder.field(NAME_FIELD.getPreferredName(), name);
        }
        if (title != null) {
            builder.field(TITLE_FIELD.getPreferredName(), title);
        }
        if (description != null) {
            builder.field(DESCRIPTION_FIELD.getPreferredName(), description);
        }
        if (mimeType != null) {
            builder.field(MIME_TYPE_FIELD.getPreferredName(), mimeType);
        }
        if (size != null) {
            builder.field(SIZE_FIELD.getPreferredName(), size);
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
