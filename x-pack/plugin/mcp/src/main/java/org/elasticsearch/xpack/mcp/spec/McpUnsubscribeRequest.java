/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

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

public record McpUnsubscribeRequest(@NotNull String uri, Map<String, Object> meta) implements McpRequest {

    private static final ParseField URI_FIELD = new ParseField("uri");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpUnsubscribeRequest, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_unsubscribe_request",
        args -> new McpUnsubscribeRequest((String) args[0], (Map<String, Object>) args[1])
    );

    static {
        PARSER.declareString(constructorArg(), URI_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return "mcp_unsubscribe_request";
    }

    public McpUnsubscribeRequest(StreamInput in) throws IOException {
        this(in.readOptionalString(), in.readMap(StreamInput::readString, StreamInput::readGenericValue));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(uri);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject();
        if (uri != null) {
            builder.field(URI_FIELD.getPreferredName(), uri);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
