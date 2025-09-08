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
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

/**
 * A notification from the server to the client, informing it that a resource has changed and may need to be read again. This should
 * only be sent if the client previously sent a resources/subscribe request.
 *
 * @param uri  The URI of the resource that has been updated. This might be a sub-resource of the one that the client actually
 *             subscribed to.
 * @param meta Additional metadata.
 */
public record McpResourcesUpdatedNotification(@NotNull String uri, Map<String, Object> meta) implements McpServerNotification {

    public static final String NAME = "mcp_resources_updated_notification";

    private static final ParseField URI_FIELD = new ParseField("uri");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpResourcesUpdatedNotification, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpResourcesUpdatedNotification((String) args[0], (Map<String, Object>) args[1])
    );

    static {
        PARSER.declareString(constructorArg(), URI_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpResourcesUpdatedNotification(StreamInput in) throws IOException {
        this(in.readString(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(uri);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(URI_FIELD.getPreferredName(), uri);
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }
}
