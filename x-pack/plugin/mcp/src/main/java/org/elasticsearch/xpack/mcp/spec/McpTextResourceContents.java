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

/**
 * The contents of a specific resource or sub-resource.
 *
 * @param uri      The URI of this resource.
 * @param mimeType The MIME type of this resource, if known.
 * @param text     The text of the item.
 * @param meta     Additional metadata.
 */
public record McpTextResourceContents(@NotNull String uri, @NotNull String mimeType, @NotNull String text, Map<String, Object> meta)
    implements
        McpResourceContent {

    public static final String NAME = "mcp_text_resource_contents";

    private static final ParseField URI_FIELD = new ParseField("uri");
    private static final ParseField MIME_TYPE_FIELD = new ParseField("mimeType");
    private static final ParseField TEXT_FIELD = new ParseField("text");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpTextResourceContents, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_text_resource_contents",
        args -> new McpTextResourceContents((String) args[0], (String) args[1], (String) args[2], (Map<String, Object>) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), URI_FIELD);
        PARSER.declareString(constructorArg(), MIME_TYPE_FIELD);
        PARSER.declareString(constructorArg(), TEXT_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    public McpTextResourceContents(StreamInput in) throws IOException {
        this(in.readString(), in.readString(), in.readString(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(uri);
        out.writeString(mimeType);
        out.writeString(text);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(URI_FIELD.getPreferredName(), uri);
        builder.field(MIME_TYPE_FIELD.getPreferredName(), mimeType);
        builder.field(TEXT_FIELD.getPreferredName(), text);
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
