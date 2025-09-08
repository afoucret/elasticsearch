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

/**
 * Capabilities that a server may support. Known capabilities are defined here, in this schema, but this is not a closed set: any
 * server can define its own, additional capabilities.
 *
 * @param experimental Experimental, non-standard capabilities that the server supports.
 * @param logging      Present if the server supports sending log messages to the client.
 * @param completions  Present if the server supports argument autocompletion suggestions.
 * @param prompts      Present if the server offers any prompt templates.
 * @param resources    Present if the server offers any resources to read.
 * @param tools        Present if the server offers any tools to call.
 */
public record McpServerCapabilities(
    CompletionCapabilities completions,
    Map<String, Object> experimental,
    LoggingCapabilities logging,
    PromptCapabilities prompts,
    ResourceCapabilities resources,
    ToolCapabilities tools
) implements NamedWriteable, ToXContentObject {

    private static final ParseField COMPLETIONS_FIELD = new ParseField("completions");
    private static final ParseField EXPERIMENTAL_FIELD = new ParseField("experimental");
    private static final ParseField LOGGING_FIELD = new ParseField("logging");
    private static final ParseField PROMPTS_FIELD = new ParseField("prompts");
    private static final ParseField RESOURCES_FIELD = new ParseField("resources");
    private static final ParseField TOOLS_FIELD = new ParseField("tools");
    public static final String NAME = "mcp_server_capabilities";

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpServerCapabilities, Object> PARSER = new ConstructingObjectParser<>(
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
        PARSER.declareObject(optionalConstructorArg(), CompletionCapabilities.PARSER, COMPLETIONS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), EXPERIMENTAL_FIELD);
        PARSER.declareObject(optionalConstructorArg(), LoggingCapabilities.PARSER, LOGGING_FIELD);
        PARSER.declareObject(optionalConstructorArg(), PromptCapabilities.PARSER, PROMPTS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), ResourceCapabilities.PARSER, RESOURCES_FIELD);
        PARSER.declareObject(optionalConstructorArg(), ToolCapabilities.PARSER, TOOLS_FIELD);
    }

    public McpServerCapabilities(StreamInput in) throws IOException {
        this(
            in.readOptionalNamedWriteable(CompletionCapabilities.class),
            in.readOptional(StreamInput::readGenericMap),
            in.readOptionalNamedWriteable(LoggingCapabilities.class),
            in.readOptionalNamedWriteable(PromptCapabilities.class),
            in.readOptionalNamedWriteable(ResourceCapabilities.class),
            in.readOptionalNamedWriteable(ToolCapabilities.class)
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalNamedWriteable(completions);
        out.writeOptional(StreamOutput::writeGenericMap, experimental);
        out.writeOptionalNamedWriteable(logging);
        out.writeOptionalNamedWriteable(prompts);
        out.writeOptionalNamedWriteable(resources);
        out.writeOptionalNamedWriteable(tools);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (completions != null) {
            builder.field(COMPLETIONS_FIELD.getPreferredName(), completions);
        }
        if (experimental != null) {
            builder.field(EXPERIMENTAL_FIELD.getPreferredName(), experimental);
        }
        if (logging != null) {
            builder.field(LOGGING_FIELD.getPreferredName(), logging);
        }
        if (prompts != null) {
            builder.field(PROMPTS_FIELD.getPreferredName(), prompts);
        }
        if (resources != null) {
            builder.field(RESOURCES_FIELD.getPreferredName(), resources);
        }
        if (tools != null) {
            builder.field(TOOLS_FIELD.getPreferredName(), tools);
        }
        builder.endObject();
        return builder;
    }

    public record CompletionCapabilities() implements NamedWriteable, ToXContentObject {
        public static final String NAME = "mcp_server_capabilities_completion_capabilities";
        public static final ObjectParser<CompletionCapabilities, Object> PARSER = new ObjectParser<>(
            NAME,
            false,
            CompletionCapabilities::new
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
        public static final String NAME = "mcp_server_capabilities_logging_capabilities";
        public static final ObjectParser<LoggingCapabilities, Object> PARSER = new ObjectParser<>(NAME, false, LoggingCapabilities::new);

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
        public static final String NAME = "mcp_server_capabilities_prompt_capabilities";

        public static final ConstructingObjectParser<PromptCapabilities, Object> PARSER = new ConstructingObjectParser<>(
            NAME,
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
        public static final String NAME = "mcp_server_capabilities_resource_capabilities";

        public static final ConstructingObjectParser<ResourceCapabilities, Object> PARSER = new ConstructingObjectParser<>(
            NAME,
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
        public static final String NAME = "mcp_server_capabilities_tool_capabilities";

        public static final ConstructingObjectParser<ToolCapabilities, Object> PARSER = new ConstructingObjectParser<>(
            NAME,
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
