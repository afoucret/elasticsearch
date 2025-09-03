/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpModelPreferences(List<McpModelHint> hints, Double costPriority, Double speedPriority, Double intelligencePriority)
    implements
        Writeable,
        ToXContentObject {

    private static final ParseField HINTS = new ParseField("hints");
    private static final ParseField COST_PRIORITY = new ParseField("costPriority");
    private static final ParseField SPEED_PRIORITY = new ParseField("speedPriority");
    private static final ParseField INTELLIGENCE_PRIORITY = new ParseField("intelligencePriority");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpModelPreferences, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_model_preferences",
        args -> new McpModelPreferences((List<McpModelHint>) args[0], (Double) args[1], (Double) args[2], (Double) args[3])
    );

    static {
        PARSER.declareObjectArray(optionalConstructorArg(), (p, c) -> McpModelHint.PARSER.parse(p, null), HINTS);
        PARSER.declareDouble(optionalConstructorArg(), COST_PRIORITY);
        PARSER.declareDouble(optionalConstructorArg(), SPEED_PRIORITY);
        PARSER.declareDouble(optionalConstructorArg(), INTELLIGENCE_PRIORITY);
    }

    public McpModelPreferences(StreamInput in) throws IOException {
        this(in.readCollectionAsList(McpModelHint::new), in.readOptionalDouble(), in.readOptionalDouble(), in.readOptionalDouble());
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeCollection(hints);
        out.writeOptionalDouble(costPriority);
        out.writeOptionalDouble(speedPriority);
        out.writeOptionalDouble(intelligencePriority);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (hints != null) {
            builder.field(HINTS.getPreferredName(), hints);
        }
        if (costPriority != null) {
            builder.field(COST_PRIORITY.getPreferredName(), costPriority);
        }
        if (speedPriority != null) {
            builder.field(SPEED_PRIORITY.getPreferredName(), speedPriority);
        }
        if (intelligencePriority != null) {
            builder.field(INTELLIGENCE_PRIORITY.getPreferredName(), intelligencePriority);
        }
        builder.endObject();
        return builder;
    }
}
