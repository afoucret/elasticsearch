/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;

public record McpResourceTemplateReference(@NotNull String uri) implements McpReference {

    public static final String NAME = "mcp_resource_template_reference";
    public static final String TYPE_VALUE = "ref/resource";

    private static final ParseField TYPE_FIELD = new ParseField("type");
    private static final ParseField URI_FIELD = new ParseField("uri");

    public static final ConstructingObjectParser<McpResourceTemplateReference, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpResourceTemplateReference((String) args[0])
    );

    static {
        PARSER.declareString(constructorArg(), URI_FIELD);
        PARSER.declareString(constructorArg(), TYPE_FIELD);
    }

    public McpResourceTemplateReference(StreamInput in) throws IOException {
        this(in.readString());
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public Type type() {
        return Type.RESOURCE;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(type());
        out.writeString(uri);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(TYPE_FIELD.getPreferredName(), TYPE_VALUE);
        builder.field(URI_FIELD.getPreferredName(), uri);
        builder.endObject();
        return builder;
    }
}
