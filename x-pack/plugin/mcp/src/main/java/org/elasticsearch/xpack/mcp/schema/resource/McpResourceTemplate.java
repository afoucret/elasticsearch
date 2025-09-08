/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.resource;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent.Params;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xpack.mcp.schema.core.McpAnnotations;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

/**
 * A template description for resources available on the server.
 *
 * @param name        Intended for programmatic or logical use.
 * @param title       Intended for UI and end-user contexts.
 * @param uriTemplate A URI template (according to RFC 6570) that can be used to construct resource URIs.
 * @param description A description of what this template is for.
 * @param mimeType    The MIME type for all resources that match this template.
 * @param annotations Optional annotations for the client.
 * @param meta        Additional metadata.
 */
public record McpResourceTemplate(
    @NotNull String uriTemplate,
    @NotNull String name,
    String title,
    String description,
    String mimeType,
    McpAnnotations annotations,
    Map<String, Object> meta
) implements NamedWriteable, ToXContentObject {

    public static final String NAME = "mcp_resource_template";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpResourceTemplate.class,
        McpResourceTemplate.NAME,
        McpResourceTemplate::new
    );

    private static final ParseField URI_TEMPLATE_FIELD = new ParseField("uriTemplate");
    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField TITLE_FIELD = new ParseField("title");
    private static final ParseField DESCRIPTION_FIELD = new ParseField("description");
    private static final ParseField MIME_TYPE_FIELD = new ParseField("mimeType");
    private static final ParseField ANNOTATIONS_FIELD = new ParseField("annotations");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpResourceTemplate, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
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
        PARSER.declareString(constructorArg(), URI_TEMPLATE_FIELD);
        PARSER.declareString(constructorArg(), NAME_FIELD);
        PARSER.declareString(optionalConstructorArg(), TITLE_FIELD);
        PARSER.declareString(optionalConstructorArg(), DESCRIPTION_FIELD);
        PARSER.declareString(optionalConstructorArg(), MIME_TYPE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpAnnotations.PARSER, ANNOTATIONS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpResourceTemplate(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
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
        out.writeString(uriTemplate);
        out.writeString(name);
        out.writeOptionalString(title);
        out.writeOptionalString(description);
        out.writeOptionalString(mimeType);
        out.writeOptionalNamedWriteable(annotations);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (uriTemplate != null) {
            builder.field(URI_TEMPLATE_FIELD.getPreferredName(), uriTemplate);
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
