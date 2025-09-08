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

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpElicitRequest(@NotNull String message, McpJsonSchema requestedSchema, Map<String, Object> meta)
    implements
        McpServerRequest {

    public static final String NAME = "mcp_elicit_request";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(McpElicitRequest.class, McpElicitRequest.NAME, McpElicitRequest::new);

    private static final ParseField MESSAGE_FIELD = new ParseField("message");
    private static final ParseField REQUESTED_SCHEMA_FIELD = new ParseField("requestedSchema");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpElicitRequest, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpElicitRequest((String) args[0], (McpJsonSchema) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), MESSAGE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpJsonSchema.PARSER, REQUESTED_SCHEMA_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpElicitRequest(StreamInput in) throws IOException {
        this(in.readString(), in.readOptionalNamedWriteable(McpJsonSchema.class), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(message);
        out.writeOptionalNamedWriteable(requestedSchema);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject();
        builder.field(MESSAGE_FIELD.getPreferredName(), message);
        if (requestedSchema != null) {
            builder.field(REQUESTED_SCHEMA_FIELD.getPreferredName(), requestedSchema);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
