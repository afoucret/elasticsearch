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
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpBlobResourceContents(@NotNull String uri, @NotNull String mimeType, @NotNull String blob, Map<String, Object> meta)
    implements
        McpResourceContent {

    private static final ParseField URI_FIELD = new ParseField("uri");
    private static final ParseField MIME_TYPE_FIELD = new ParseField("mimeType");
    private static final ParseField BLOB_FIELD = new ParseField("blob");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpBlobResourceContents, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_blob_resource_contents",
        args -> new McpBlobResourceContents((String) args[0], (String) args[1], (String) args[2], (Map<String, Object>) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), URI_FIELD);
        PARSER.declareString(constructorArg(), MIME_TYPE_FIELD);
        PARSER.declareString(constructorArg(), BLOB_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpBlobResourceContents(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readOptionalString(),
            in.readOptionalString(),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(uri);
        out.writeOptionalString(mimeType);
        out.writeOptionalString(blob);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (uri != null) {
            builder.field(URI_FIELD.getPreferredName(), uri);
        }
        if (mimeType != null) {
            builder.field(MIME_TYPE_FIELD.getPreferredName(), mimeType);
        }
        if (blob != null) {
            builder.field(BLOB_FIELD.getPreferredName(), blob);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }

    @Override
    public String getWriteableName() {
        return "blob";
    }
}
