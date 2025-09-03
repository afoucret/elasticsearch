/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpAnnotations(List<McpRole> audience, Double priority) implements NamedWriteable, ToXContentObject {

    private static final ParseField AUDIENCE_FIELD = new ParseField("audience");
    private static final ParseField PRIORITY_FIELD = new ParseField("priority");
    public static final String NAME = "mcp_annotations";

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpAnnotations, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_annotations",
        args -> new McpAnnotations(
            args[0] == null ? null : ((List<String>) args[0]).stream().map(McpRole::valueOf).collect(Collectors.toList()),
            (Double) args[1]
        )
    );

    static {
        PARSER.declareStringArray(optionalConstructorArg(), AUDIENCE_FIELD);
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
        out.writeCollection(audience);
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
