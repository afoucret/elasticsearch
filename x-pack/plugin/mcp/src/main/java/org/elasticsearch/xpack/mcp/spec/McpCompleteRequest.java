/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCompleteRequest(@NotNull McpReference ref, @NotNull Argument argument, Context context, Map<String, Object> meta)
    implements
        McpClientRequest {

    public static final String NAME = "mcp_complete_request";

    private static final ParseField REF_FIELD = new ParseField("ref");
    private static final ParseField ARGUMENT_FIELD = new ParseField("argument");
    private static final ParseField CONTEXT_FIELD = new ParseField("context");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCompleteRequest, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpCompleteRequest((McpReference) args[0], (Argument) args[1], (Context) args[2], (Map<String, Object>) args[3])
    );

    static {
        PARSER.declareObject(constructorArg(), (p, c) -> McpReference.fromXContent(p), REF_FIELD);
        PARSER.declareObject(constructorArg(), Argument.PARSER, ARGUMENT_FIELD);
        PARSER.declareObject(optionalConstructorArg(), Context.PARSER, CONTEXT_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpCompleteRequest(StreamInput in) throws IOException {
        this(
            in.readNamedWriteable(McpReference.class),
            in.readNamedWriteable(Argument.class),
            in.readOptionalNamedWriteable(Context.class),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeNamedWriteable(ref);
        out.writeNamedWriteable(argument);
        out.writeOptionalNamedWriteable(context);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject();
        builder.field(REF_FIELD.getPreferredName(), ref);
        builder.field(ARGUMENT_FIELD.getPreferredName(), argument);
        if (context != null) {
            builder.field(CONTEXT_FIELD.getPreferredName(), context);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }

    public record Argument(@NotNull String name, @NotNull String value) implements NamedWriteable, ToXContentObject {
        public static final String NAME = "mcp_complete_request_argument";

        private static final ParseField NAME_FIELD = new ParseField("name");
        private static final ParseField VALUE_FIELD = new ParseField("value");

        public static final ConstructingObjectParser<Argument, Object> PARSER = new ConstructingObjectParser<>(
            NAME,
            args -> new Argument((String) args[0], (String) args[1])
        );

        static {
            PARSER.declareString(constructorArg(), NAME_FIELD);
            PARSER.declareString(constructorArg(), VALUE_FIELD);
        }

        public Argument(StreamInput in) throws IOException {
            this(in.readString(), in.readString());
        }

        @Override
        public String getWriteableName() {
            return NAME;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeString(name);
            out.writeString(value);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            builder.field(NAME_FIELD.getPreferredName(), name);
            builder.field(VALUE_FIELD.getPreferredName(), value);
            builder.endObject();
            return builder;
        }
    }

    public record Context(Map<String, Object> arguments) implements NamedWriteable, ToXContentObject {
        public static final String NAME = "mcp_complete_request_context";

        private static final ParseField ARGUMENTS_FIELD = new ParseField("arguments");

        @SuppressWarnings("unchecked")
        public static final ConstructingObjectParser<Context, Object> PARSER = new ConstructingObjectParser<>(
            NAME,
            args -> new Context((Map<String, Object>) args[0])
        );

        static {
            PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), ARGUMENTS_FIELD);
        }

        public Context(StreamInput in) throws IOException {
            this(in.readOptional(StreamInput::readGenericMap));
        }

        @Override
        public String getWriteableName() {
            return NAME;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeOptional(StreamOutput::writeGenericMap, arguments);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            if (arguments != null) {
                builder.field(ARGUMENTS_FIELD.getPreferredName(), arguments);
            }
            builder.endObject();
            return builder;
        }
    }
}
