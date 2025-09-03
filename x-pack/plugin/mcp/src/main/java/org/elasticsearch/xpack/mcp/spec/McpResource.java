/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

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

public record McpResource(
    String uri,
    String name,
    String title,
    String description,
    String mimeType,
    Long size,
    McpAnnotations annotations,
    Map<String, Object> meta
) implements NamedWriteable, ToXContentObject {

    private static final ParseField URI = new ParseField("uri");
    private static final ParseField NAME = new ParseField("name");
    private static final ParseField TITLE = new ParseField("title");
    private static final ParseField DESCRIPTION = new ParseField("description");
    private static final ParseField MIME_TYPE = new ParseField("mimeType");
    private static final ParseField SIZE = new ParseField("size");
    private static final ParseField ANNOTATIONS = new ParseField("annotations");
    private static final ParseField META = new ParseField("_meta");
    public static final String NAME_FIELD = "mcp_resource";

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpResource, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_resource",
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
        PARSER.declareString(constructorArg(), URI);
        PARSER.declareString(constructorArg(), NAME);
        PARSER.declareString(optionalConstructorArg(), TITLE);
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION);
        PARSER.declareString(optionalConstructorArg(), MIME_TYPE);
        PARSER.declareLong(optionalConstructorArg(), SIZE);
        PARSER.declareObject(optionalConstructorArg(), McpAnnotations.PARSER, ANNOTATIONS);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    public McpResource(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalLong(),
            in.readOptionalWriteable(McpAnnotations::new),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME_FIELD;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(uri);
        out.writeOptionalString(name);
        out.writeOptionalString(title);
        out.writeOptionalString(description);
        out.writeOptionalString(mimeType);
        out.writeOptionalLong(size);
        out.writeOptionalWriteable(annotations);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (uri != null) {
            builder.field(URI.getPreferredName(), uri);
        }
        if (name != null) {
            builder.field(NAME.getPreferredName(), name);
        }
        if (title != null) {
            builder.field(TITLE.getPreferredName(), title);
        }
        if (description != null) {
            builder.field(DESCRIPTION.getPreferredName(), description);
        }
        if (mimeType != null) {
            builder.field(MIME_TYPE.getPreferredName(), mimeType);
        }
        if (size != null) {
            builder.field(SIZE.getPreferredName(), size);
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
