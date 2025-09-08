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
import java.util.Locale;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpAudioContent(@NotNull String data, @NotNull String mimeType, McpAnnotations annotations, Map<String, Object> meta)
    implements
        McpContent {

    public static final String NAME = "audio";

    private static final ParseField DATA_FIELD = new ParseField("data");
    private static final ParseField MIME_TYPE_FIELD = new ParseField("mimeType");
    private static final ParseField ANNOTATIONS_FIELD = new ParseField("annotations");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpAudioContent, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpAudioContent((String) args[0], (String) args[1], (McpAnnotations) args[2], (Map<String, Object>) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), DATA_FIELD);
        PARSER.declareString(constructorArg(), MIME_TYPE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpAnnotations.PARSER, ANNOTATIONS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
        PARSER.declareString(constructorArg(), TYPE_FIELD);
    }

    public McpAudioContent(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readString(),
            in.readOptionalNamedWriteable(McpAnnotations.class),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public Type type() {
        return Type.AUDIO;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(data);
        out.writeString(mimeType);
        out.writeOptionalNamedWriteable(annotations);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(TYPE_FIELD.getPreferredName(), type().name().toLowerCase(Locale.ROOT));
        builder.field(DATA_FIELD.getPreferredName(), data);
        builder.field(MIME_TYPE_FIELD.getPreferredName(), mimeType);
        if (annotations != null) {
            builder.field(ANNOTATIONS_FIELD.getPreferredName(), annotations);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
