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
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpClientCapabilities(Map<String, Object> experimental, RootCapabilities roots, Sampling sampling, Elicitation elicitation)
    implements
        NamedWriteable,
        ToXContentObject {

    private static final ParseField EXPERIMENTAL = new ParseField("experimental");
    private static final ParseField ROOTS = new ParseField("roots");
    private static final ParseField SAMPLING = new ParseField("sampling");
    private static final ParseField ELICITATION = new ParseField("elicitation");
    public static final String NAME = "mcp_client_capabilities";

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpClientCapabilities, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_client_capabilities",
        args -> new McpClientCapabilities(
            (Map<String, Object>) args[0],
            (RootCapabilities) args[1],
            (Sampling) args[2],
            (Elicitation) args[3]
        )
    );

    static {
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), EXPERIMENTAL);
        PARSER.declareObject(optionalConstructorArg(), RootCapabilities.PARSER, ROOTS);
        PARSER.declareObject(optionalConstructorArg(), Sampling.PARSER, SAMPLING);
        PARSER.declareObject(optionalConstructorArg(), Elicitation.PARSER, ELICITATION);
    }

    public McpClientCapabilities(StreamInput in) throws IOException {
        this(
            in.readMap(StreamInput::readString, StreamInput::readGenericValue),
            in.readOptionalWriteable(RootCapabilities::new),
            in.readOptionalWriteable(Sampling::new),
            in.readOptionalWriteable(Elicitation::new)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeMap(experimental, StreamOutput::writeString, StreamOutput::writeGenericValue);
        out.writeOptionalWriteable(roots);
        out.writeOptionalWriteable(sampling);
        out.writeOptionalWriteable(elicitation);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (experimental != null) {
            builder.field(EXPERIMENTAL.getPreferredName(), experimental);
        }
        if (roots != null) {
            builder.field(ROOTS.getPreferredName(), roots);
        }
        if (sampling != null) {
            builder.field(SAMPLING.getPreferredName(), sampling);
        }
        if (elicitation != null) {
            builder.field(ELICITATION.getPreferredName(), elicitation);
        }
        builder.endObject();
        return builder;
    }

    public record RootCapabilities(Boolean listChanged) implements NamedWriteable, ToXContentObject {

        private static final ParseField LIST_CHANGED = new ParseField("listChanged");
        public static final String NAME = "root_capabilities";

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

        public static final String NAME = "sampling";

        public static final ConstructingObjectParser<Sampling, Void> PARSER = new ConstructingObjectParser<>(
            "sampling",
            args -> new Sampling()
        );

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

        public static final String NAME = "elicitation";

        public static final ConstructingObjectParser<Elicitation, Void> PARSER = new ConstructingObjectParser<>(
            "elicitation",
            args -> new Elicitation()
        );

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
