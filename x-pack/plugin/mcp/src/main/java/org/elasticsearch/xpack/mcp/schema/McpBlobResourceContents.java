/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
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

    public static final String NAME = "mcp_blob_resource_contents";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpBlobResourceContents.class,
        McpBlobResourceContents.NAME,
        McpBlobResourceContents::new
    );

    private static final ParseField URI_FIELD = new ParseField("uri");
    private static final ParseField MIME_TYPE_FIELD = new ParseField("mimeType");
    protected static final ParseField BLOB_FIELD = new ParseField("blob");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpBlobResourceContents, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpBlobResourceContents((String) args[0], (String) args[1], (String) args[2], (Map<String, Object>) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), URI_FIELD);
        PARSER.declareString(constructorArg(), MIME_TYPE_FIELD);
        PARSER.declareString(constructorArg(), BLOB_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpBlobResourceContents(StreamInput in) throws IOException {
        this(in.readString(), in.readString(), in.readString(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(uri);
        out.writeString(mimeType);
        out.writeString(blob);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(URI_FIELD.getPreferredName(), uri);
        builder.field(MIME_TYPE_FIELD.getPreferredName(), mimeType);
        builder.field(BLOB_FIELD.getPreferredName(), blob);
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
