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

public record McpServerCapabilities(
    CompletionCapabilities completions,
    Map<String, Object> experimental,
    LoggingCapabilities logging,
    PromptCapabilities prompts,
    ResourceCapabilities resources,
    ToolCapabilities tools
) implements NamedWriteable, ToXContentObject {

    private static final ParseField COMPLETIONS = new ParseField("completions");
    private static final ParseField EXPERIMENTAL = new ParseField("experimental");
    private static final ParseField LOGGING = new ParseField("logging");
    private static final ParseField PROMPTS = new ParseField("prompts");
    private static final ParseField RESOURCES = new ParseField("resources");
    private static final ParseField TOOLS = new ParseField("tools");
    public static final String NAME = "mcp_server_capabilities";

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpServerCapabilities, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_server_capabilities",
        args -> new McpServerCapabilities(
            (CompletionCapabilities) args[0],
            (Map<String, Object>) args[1],
            (LoggingCapabilities) args[2],
            (PromptCapabilities) args[3],
            (ResourceCapabilities) args[4],
            (ToolCapabilities) args[5]
        )
    );

    static {
        PARSER.declareObject(optionalConstructorArg(), CompletionCapabilities.PARSER, COMPLETIONS);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), EXPERIMENTAL);
        PARSER.declareObject(optionalConstructorArg(), LoggingCapabilities.PARSER, LOGGING);
        PARSER.declareObject(optionalConstructorArg(), PromptCapabilities.PARSER, PROMPTS);
        PARSER.declareObject(optionalConstructorArg(), ResourceCapabilities.PARSER, RESOURCES);
        PARSER.declareObject(optionalConstructorArg(), ToolCapabilities.PARSER, TOOLS);
    }

    public McpServerCapabilities(StreamInput in) throws IOException {
        this(
            in.readOptionalWriteable(CompletionCapabilities::new),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue),
            in.readOptionalWriteable(LoggingCapabilities::new),
            in.readOptionalWriteable(PromptCapabilities::new),
            in.readOptionalWriteable(ResourceCapabilities::new),
            in.readOptionalWriteable(ToolCapabilities::new)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalWriteable(completions);
        out.writeMap(experimental, StreamOutput::writeString, StreamOutput::writeGenericValue);
        out.writeOptionalWriteable(logging);
        out.writeOptionalWriteable(prompts);
        out.writeOptionalWriteable(resources);
        out.writeOptionalWriteable(tools);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (completions != null) {
            builder.field(COMPLETIONS.getPreferredName(), completions);
        }
        if (experimental != null) {
            builder.field(EXPERIMENTAL.getPreferredName(), experimental);
        }
        if (logging != null) {
            builder.field(LOGGING.getPreferredName(), logging);
        }
        if (prompts != null) {
            builder.field(PROMPTS.getPreferredName(), prompts);
        }
        if (resources != null) {
            builder.field(RESOURCES.getPreferredName(), resources);
        }
        if (tools != null) {
            builder.field(TOOLS.getPreferredName(), tools);
        }
        builder.endObject();
        return builder;
    }

    public record CompletionCapabilities() implements NamedWriteable, ToXContentObject {
        public static final String NAME = "completion_capabilities";
        public static final ConstructingObjectParser<CompletionCapabilities, Void> PARSER = new ConstructingObjectParser<>(
            "completion_capabilities",
            args -> new CompletionCapabilities()
        );

        public CompletionCapabilities(StreamInput in) {
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

    public record LoggingCapabilities() implements NamedWriteable, ToXContentObject {
        public static final String NAME = "logging_capabilities";
        public static final ConstructingObjectParser<LoggingCapabilities, Void> PARSER = new ConstructingObjectParser<>(
            "logging_capabilities",
            args -> new LoggingCapabilities()
        );

        public LoggingCapabilities(StreamInput in) {
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

    public record PromptCapabilities(Boolean listChanged) implements NamedWriteable, ToXContentObject {
        private static final ParseField LIST_CHANGED = new ParseField("listChanged");
        public static final String NAME = "prompt_capabilities";

        public static final ConstructingObjectParser<PromptCapabilities, Void> PARSER = new ConstructingObjectParser<>(
            "prompt_capabilities",
            args -> new PromptCapabilities((Boolean) args[0])
        );

        static {
            PARSER.declareBoolean(optionalConstructorArg(), LIST_CHANGED);
        }

        public PromptCapabilities(StreamInput in) throws IOException {
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

    public record ResourceCapabilities(Boolean subscribe, Boolean listChanged) implements NamedWriteable, ToXContentObject {
        private static final ParseField SUBSCRIBE = new ParseField("subscribe");
        private static final ParseField LIST_CHANGED = new ParseField("listChanged");
        public static final String NAME = "resource_capabilities";

        public static final ConstructingObjectParser<ResourceCapabilities, Void> PARSER = new ConstructingObjectParser<>(
            "resource_capabilities",
            args -> new ResourceCapabilities((Boolean) args[0], (Boolean) args[1])
        );

        static {
            PARSER.declareBoolean(optionalConstructorArg(), SUBSCRIBE);
            PARSER.declareBoolean(optionalConstructorArg(), LIST_CHANGED);
        }

        public ResourceCapabilities(StreamInput in) throws IOException {
            this(in.readOptionalBoolean(), in.readOptionalBoolean());
        }

        @Override
        public String getWriteableName() {
            return NAME;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeOptionalBoolean(subscribe);
            out.writeOptionalBoolean(listChanged);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            if (subscribe != null) {
                builder.field(SUBSCRIBE.getPreferredName(), subscribe);
            }
            if (listChanged != null) {
                builder.field(LIST_CHANGED.getPreferredName(), listChanged);
            }
            builder.endObject();
            return builder;
        }
    }

    public record ToolCapabilities(Boolean listChanged) implements NamedWriteable, ToXContentObject {
        private static final ParseField LIST_CHANGED = new ParseField("listChanged");
        public static final String NAME = "tool_capabilities";

        public static final ConstructingObjectParser<ToolCapabilities, Void> PARSER = new ConstructingObjectParser<>(
            "tool_capabilities",
            args -> new ToolCapabilities((Boolean) args[0])
        );

        static {
            PARSER.declareBoolean(optionalConstructorArg(), LIST_CHANGED);
        }

        public ToolCapabilities(StreamInput in) throws IOException {
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
}
