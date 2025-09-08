/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.*;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpInitializeResult(
    @NotNull String protocolVersion,
    McpServerCapabilities capabilities,
    McpImplementation serverInfo,
    String instructions,
    Map<String, Object> meta
) implements McpServerResult {

    public static final String NAME = "mcp_initialize_result";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(
        McpInitializeResult.class,
        McpInitializeResult.NAME,
        McpInitializeResult::new
    );

    private static final ParseField PROTOCOL_VERSION_FIELD = new ParseField("protocolVersion");
    private static final ParseField CAPABILITIES_FIELD = new ParseField("capabilities");
    private static final ParseField SERVER_INFO_FIELD = new ParseField("serverInfo");
    private static final ParseField INSTRUCTIONS_FIELD = new ParseField("instructions");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpInitializeResult, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpInitializeResult(
            (String) args[0],
            (McpServerCapabilities) args[1],
            (McpImplementation) args[2],
            (String) args[3],
            (Map<String, Object>) args[4]
        )
    );

    static {
        PARSER.declareString(constructorArg(), PROTOCOL_VERSION_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpServerCapabilities.PARSER, CAPABILITIES_FIELD);
        PARSER.declareObject(optionalConstructorArg(), McpImplementation.PARSER, SERVER_INFO_FIELD);
        PARSER.declareString(optionalConstructorArg(), INSTRUCTIONS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpInitializeResult(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readOptionalNamedWriteable(McpServerCapabilities.class),
            in.readOptionalNamedWriteable(McpImplementation.class),
            in.readOptionalString(),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(protocolVersion);
        out.writeOptionalNamedWriteable(capabilities);
        out.writeOptionalNamedWriteable(serverInfo);
        out.writeOptionalString(instructions);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (protocolVersion != null) {
            builder.field(PROTOCOL_VERSION_FIELD.getPreferredName(), protocolVersion);
        }
        if (capabilities != null) {
            builder.field(CAPABILITIES_FIELD.getPreferredName(), capabilities);
        }
        if (serverInfo != null) {
            builder.field(SERVER_INFO_FIELD.getPreferredName(), serverInfo);
        }
        if (instructions != null) {
            builder.field(INSTRUCTIONS_FIELD.getPreferredName(), instructions);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
