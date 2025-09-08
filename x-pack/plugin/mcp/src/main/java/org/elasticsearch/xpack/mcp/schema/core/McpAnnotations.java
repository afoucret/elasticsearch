/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpAnnotations(List<McpRole> audience, Double priority) implements NamedWriteable, ToXContentObject {

    public static final String NAME = "mcp_annotations";

    private static final ParseField AUDIENCE_FIELD = new ParseField("audience");
    private static final ParseField PRIORITY_FIELD = new ParseField("priority");

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(McpAnnotations.class, McpAnnotations.NAME, McpAnnotations::new);

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpAnnotations, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpAnnotations((List<McpRole>) args[0], (Double) args[1])
    );

    static {
        PARSER.declareFieldArray(optionalConstructorArg(), (p, c) -> {
            try {
                return McpRole.valueOf(p.text().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new ParsingException(
                    p.getTokenLocation(),
                    "Invalid audience [" + p.text() + "], allowed values are " + List.of(McpRole.values())
                );
            }
        }, AUDIENCE_FIELD, ObjectParser.ValueType.STRING_ARRAY);
        PARSER.declareDouble(optionalConstructorArg(), PRIORITY_FIELD);
    }

    public McpAnnotations(StreamInput in) throws IOException {
        this(in.readOptionalCollectionAsList(McpRole::readFrom), in.readOptionalDouble());
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalCollection(audience);
        out.writeOptionalDouble(priority);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (audience != null) {
            builder.field(AUDIENCE_FIELD.getPreferredName(), audience);
        }
        if (priority != null) {
            builder.field(PRIORITY_FIELD.getPreferredName(), priority);
        }
        builder.endObject();
        return builder;
    }
}
