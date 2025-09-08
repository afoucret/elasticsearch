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

/**
 * Text provided to or from an LLM.
 *
 * @param text        The text content of the message.
 * @param annotations Optional annotations for the client.
 * @param meta        Additional metadata.
 */
public record McpTextContent(@NotNull String text, McpAnnotations annotations, Map<String, Object> meta) implements McpContent {

    public static final String NAME = "mcp_text_content";

    private static final ParseField TEXT_FIELD = new ParseField("text");
    private static final ParseField ANNOTATIONS_FIELD = new ParseField("annotations");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpTextContent, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpTextContent((String) args[0], (McpAnnotations) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), TEXT_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpAnnotations.PARSER, ANNOTATIONS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
        PARSER.declareString(constructorArg(), TYPE_FIELD);
    }

    public McpTextContent(StreamInput in) throws IOException {
        this(in.readString(), in.readOptionalNamedWriteable(McpAnnotations.class), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public Type type() {
        return Type.TEXT;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(text);
        out.writeOptionalNamedWriteable(annotations);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(TYPE_FIELD.getPreferredName(), type().name().toLowerCase(Locale.ROOT));
        builder.field(TEXT_FIELD.getPreferredName(), text);
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
