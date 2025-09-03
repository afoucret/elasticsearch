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
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpInitializeResult(
    String protocolVersion,
    McpServerCapabilities capabilities,
    McpImplementation serverInfo,
    String instructions,
    Map<String, Object> meta
) implements Writeable, ToXContentObject {

    private static final ParseField PROTOCOL_VERSION = new ParseField("protocolVersion");
    private static final ParseField CAPABILITIES = new ParseField("capabilities");
    private static final ParseField SERVER_INFO = new ParseField("serverInfo");
    private static final ParseField INSTRUCTIONS = new ParseField("instructions");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpInitializeResult, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_initialize_result",
        args -> new McpInitializeResult(
            (String) args[0],
            (McpServerCapabilities) args[1],
            (McpImplementation) args[2],
            (String) args[3],
            (Map<String, Object>) args[4]
        )
    );

    static {
        PARSER.declareString(constructorArg(), PROTOCOL_VERSION);
        PARSER.declareObject(optionalConstructorArg(), McpServerCapabilities.PARSER, CAPABILITIES);
        PARSER.declareObject(optionalConstructorArg(), McpImplementation.PARSER, SERVER_INFO);
        PARSER.declareString(optionalConstructorArg(), INSTRUCTIONS);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    public McpInitializeResult(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readOptionalWriteable(McpServerCapabilities::new),
            in.readOptionalWriteable(McpImplementation::new),
            in.readOptionalString(),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(protocolVersion);
        out.writeOptionalWriteable(capabilities);
        out.writeOptionalWriteable(serverInfo);
        out.writeOptionalString(instructions);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (protocolVersion != null) {
            builder.field(PROTOCOL_VERSION.getPreferredName(), protocolVersion);
        }
        if (capabilities != null) {
            builder.field(CAPABILITIES.getPreferredName(), capabilities);
        }
        if (serverInfo != null) {
            builder.field(SERVER_INFO.getPreferredName(), serverInfo);
        }
        if (instructions != null) {
            builder.field(INSTRUCTIONS.getPreferredName(), instructions);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
