/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.content;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xpack.mcp.schema.core.McpAnnotations;
import org.elasticsearch.xpack.mcp.schema.resource.McpResourceContent;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpEmbeddedResource(@NotNull McpResourceContent resource, McpAnnotations annotations, Map<String, Object> meta)
    implements
        McpContent {

    public static final String NAME = "mcp_embedded_resource";
    public static final String TYPE_VALUE = "resource";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(McpContent.class, McpEmbeddedResource.NAME, McpEmbeddedResource::new);

    private static final ParseField TYPE_FIELD = new ParseField("type");
    private static final ParseField RESOURCE_FIELD = new ParseField("resource");
    private static final ParseField ANNOTATIONS_FIELD = new ParseField("annotations");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpEmbeddedResource, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpEmbeddedResource((McpResourceContent) args[0], (McpAnnotations) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareObject(constructorArg(), (p, c) -> McpResourceContent.fromXContent(p), RESOURCE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpAnnotations.PARSER, ANNOTATIONS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
        PARSER.declareString(optionalConstructorArg(), TYPE_FIELD);
    }

    public McpEmbeddedResource(StreamInput in) throws IOException {
        this(
            in.readNamedWriteable(McpResourceContent.class),
            in.readOptionalNamedWriteable(McpAnnotations.class),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public Type type() {
        return Type.EMBEDDED_RESOURCE;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeNamedWriteable(resource);
        out.writeOptionalNamedWriteable(annotations);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(TYPE_FIELD.getPreferredName(), TYPE_VALUE);
        builder.field(RESOURCE_FIELD.getPreferredName(), resource);
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
