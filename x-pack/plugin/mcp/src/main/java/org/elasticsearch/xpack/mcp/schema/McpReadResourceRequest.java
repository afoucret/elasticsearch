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
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

/**
 * Sent from the client to the server, to read a specific resource URI.
 *
 * @param uri  The URI of the resource to read. The URI can use any protocol; it is up to the server how to interpret it.
 * @param meta Additional metadata.
 */
public record McpReadResourceRequest(@NotNull String uri, Map<String, Object> meta) implements McpClientRequest {

    public static final String NAME = "mcp_read_resource_request";

    private static final ParseField URI_FIELD = new ParseField("uri");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpReadResourceRequest, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpReadResourceRequest((String) args[0], (Map<String, Object>) args[1])
    );

    static {
        PARSER.declareString(constructorArg(), URI_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpReadResourceRequest(StreamInput in) throws IOException {
        this(in.readString(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(uri);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject();
        builder.field(URI_FIELD.getPreferredName(), uri);
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
