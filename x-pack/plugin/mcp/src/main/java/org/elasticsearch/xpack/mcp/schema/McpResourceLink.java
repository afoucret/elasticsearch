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
import java.util.Locale;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;

/**
 * A resource that the server is capable of reading, included in a prompt or tool call result.
 * <p>
 * Note: resource links returned by tools are not guaranteed to appear in the results of `resources/list` requests.
 */
public record McpResourceLink(@NotNull String uri) implements McpContent {

    public static final String NAME = "mcp_resource_link";

    private static final ParseField URI = new ParseField("uri");

    public static final ConstructingObjectParser<McpResourceLink, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpResourceLink((String) args[0])
    );

    static {
        PARSER.declareString(constructorArg(), URI);
        PARSER.declareString(constructorArg(), TYPE_FIELD);
    }

    public McpResourceLink(StreamInput in) throws IOException {
        this(in.readString());
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public Type type() {
        return Type.RESOURCE_LINK;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(uri);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(TYPE_FIELD.getPreferredName(), type().name().toLowerCase(Locale.ROOT));
        builder.field(URI.getPreferredName(), uri);
        builder.endObject();
        return builder;
    }
}
