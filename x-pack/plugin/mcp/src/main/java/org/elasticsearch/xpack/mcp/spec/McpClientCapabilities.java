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
import org.elasticsearch.xcontent.ObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpClientCapabilities(Map<String, Object> experimental, RootCapabilities roots, Sampling sampling, Elicitation elicitation)
    implements
        NamedWriteable,
        ToXContentObject {

    public static final String NAME = "mcp_client_capabilities";

    private static final ParseField EXPERIMENTAL_FIELD = new ParseField("experimental");
    private static final ParseField ROOTS_FIELD = new ParseField("roots");
    private static final ParseField SAMPLING_FIELD = new ParseField("sampling");
    private static final ParseField ELICITATION_FIELD = new ParseField("elicitation");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpClientCapabilities, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpClientCapabilities(
            (Map<String, Object>) args[0],
            (RootCapabilities) args[1],
            (Sampling) args[2],
            (Elicitation) args[3]
        )
    );

    static {
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), EXPERIMENTAL_FIELD);
        PARSER.declareObject(optionalConstructorArg(), RootCapabilities.PARSER, ROOTS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), Sampling.PARSER, SAMPLING_FIELD);
        PARSER.declareObject(optionalConstructorArg(), Elicitation.PARSER, ELICITATION_FIELD);
    }

    public McpClientCapabilities(StreamInput in) throws IOException {
        this(
            in.readOptional(StreamInput::readGenericMap),
            in.readOptionalNamedWriteable(RootCapabilities.class),
            in.readOptionalNamedWriteable(Sampling.class),
            in.readOptionalNamedWriteable(Elicitation.class)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptional(StreamOutput::writeGenericMap, experimental);
        out.writeOptionalNamedWriteable(roots);
        out.writeOptionalNamedWriteable(sampling);
        out.writeOptionalNamedWriteable(elicitation);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (experimental != null) {
            builder.field(EXPERIMENTAL_FIELD.getPreferredName(), experimental);
        }
        if (roots != null) {
            builder.field(ROOTS_FIELD.getPreferredName(), roots);
        }
        if (sampling != null) {
            builder.field(SAMPLING_FIELD.getPreferredName(), sampling);
        }
        if (elicitation != null) {
            builder.field(ELICITATION_FIELD.getPreferredName(), elicitation);
        }
        builder.endObject();
        return builder;
    }

    public record RootCapabilities(Boolean listChanged) implements NamedWriteable, ToXContentObject {

        private static final ParseField LIST_CHANGED = new ParseField("listChanged");
        public static final String NAME = "mcp_client_capabilities_root_capabilities";

        public static final ConstructingObjectParser<RootCapabilities, Void> PARSER = new ConstructingObjectParser<>(
            "root_capabilities",
            args -> new RootCapabilities((Boolean) args[0])
        );

        static {
            PARSER.declareBoolean(optionalConstructorArg(), LIST_CHANGED);
        }

        public RootCapabilities(StreamInput in) throws IOException {
            this(in.readOptionalBoolean());
        }

        @Override
        public String getWriteableName() {
            return NAME;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeOptionalBoolean(listChanged);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            if (listChanged != null) {
                builder.field(LIST_CHANGED.getPreferredName(), listChanged);
            }
            builder.endObject();
            return builder;
        }
    }

    public record Sampling() implements NamedWriteable, ToXContentObject {

        public static final String NAME = "mcp_client_capabilities_sampling";

        public static final ObjectParser<Sampling, Void> PARSER = new ObjectParser<>(NAME, Sampling::new);

        public Sampling(StreamInput in) {
            this();
        }

        @Override
        public String getWriteableName() {
            return NAME;
        }

        @Override
        public void writeTo(StreamOutput out) {}

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            builder.endObject();
            return builder;
        }
    }

    public record Elicitation() implements NamedWriteable, ToXContentObject {

        public static final String NAME = "mcp_client_capabilities_elicitation";

        public static final ObjectParser<Elicitation, Void> PARSER = new ObjectParser<>(NAME, Elicitation::new);

        public Elicitation(StreamInput in) {
            this();
        }

        @Override
        public String getWriteableName() {
            return NAME;
        }

        @Override
        public void writeTo(StreamOutput out) {}

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            builder.endObject();
            return builder;
        }
    }
}
