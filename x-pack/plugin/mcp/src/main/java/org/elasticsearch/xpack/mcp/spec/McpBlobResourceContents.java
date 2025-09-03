/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpBlobResourceContents(String uri, String mimeType, String blob, Map<String, Object> meta) implements McpResourceContent {

    private static final ParseField URI = new ParseField("uri");
    private static final ParseField MIME_TYPE = new ParseField("mimeType");
    private static final ParseField BLOB = new ParseField("blob");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpBlobResourceContents, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_blob_resource_contents",
        args -> new McpBlobResourceContents((String) args[0], (String) args[1], (String) args[2], (Map<String, Object>) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), URI);
        PARSER.declareString(constructorArg(), MIME_TYPE);
        PARSER.declareString(constructorArg(), BLOB);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
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
            builder.field(URI.getPreferredName(), uri);
        }
        if (mimeType != null) {
            builder.field(MIME_TYPE.getPreferredName(), mimeType);
        }
        if (blob != null) {
            builder.field(BLOB.getPreferredName(), blob);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }

    @Override
    public String getWriteableName() {
        return "blob";
    }
}
