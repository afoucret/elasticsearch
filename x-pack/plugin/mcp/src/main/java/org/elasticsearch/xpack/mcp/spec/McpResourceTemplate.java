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

public record McpResourceTemplate(
    String uriTemplate,
    String name,
    String title,
    String description,
    String mimeType,
    McpAnnotations annotations,
    Map<String, Object> meta
) implements NamedWriteable, ToXContentObject {

    private static final ParseField URI_TEMPLATE = new ParseField("uriTemplate");
    private static final ParseField NAME = new ParseField("name");
    private static final ParseField TITLE = new ParseField("title");
    private static final ParseField DESCRIPTION = new ParseField("description");
    private static final ParseField MIME_TYPE = new ParseField("mimeType");
    private static final ParseField ANNOTATIONS = new ParseField("annotations");
    private static final ParseField META = new ParseField("_meta");
    public static final String NAME_FIELD = "mcp_resource_template";

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpResourceTemplate, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_resource_template",
        args -> new McpResourceTemplate(
            (String) args[0],
            (String) args[1],
            (String) args[2],
            (String) args[3],
            (String) args[4],
            (McpAnnotations) args[5],
            (Map<String, Object>) args[6]
        )
    );

    static {
        PARSER.declareString(constructorArg(), URI_TEMPLATE);
        PARSER.declareString(constructorArg(), NAME);
        PARSER.declareString(optionalConstructorArg(), TITLE);
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION);
        PARSER.declareString(optionalConstructorArg(), MIME_TYPE);
        PARSER.declareObject(optionalConstructorArg(), McpAnnotations.PARSER, ANNOTATIONS);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    public McpResourceTemplate(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
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
        out.writeOptionalString(uriTemplate);
        out.writeOptionalString(name);
        out.writeOptionalString(title);
        out.writeOptionalString(description);
        out.writeOptionalString(mimeType);
        out.writeOptionalWriteable(annotations);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (uriTemplate != null) {
            builder.field(URI_TEMPLATE.getPreferredName(), uriTemplate);
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
