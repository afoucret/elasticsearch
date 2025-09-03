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

public record McpInitializeRequest(
    String protocolVersion,
    McpClientCapabilities capabilities,
    McpImplementation clientInfo,
    Map<String, Object> meta
) implements Writeable, ToXContentObject {

    private static final ParseField PROTOCOL_VERSION = new ParseField("protocolVersion");
    private static final ParseField CAPABILITIES = new ParseField("capabilities");
    private static final ParseField CLIENT_INFO = new ParseField("clientInfo");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpInitializeRequest, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_initialize_request",
        args -> new McpInitializeRequest(
            (String) args[0],
            (McpClientCapabilities) args[1],
            (McpImplementation) args[2],
            (Map<String, Object>) args[3]
        )
    );

    static {
        PARSER.declareString(constructorArg(), PROTOCOL_VERSION);
        PARSER.declareObject(optionalConstructorArg(), McpClientCapabilities.PARSER, CAPABILITIES);
        PARSER.declareObject(optionalConstructorArg(), McpImplementation.PARSER, CLIENT_INFO);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    public McpInitializeRequest(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readOptionalWriteable(McpClientCapabilities::new),
            in.readOptionalWriteable(McpImplementation::new),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(protocolVersion);
        out.writeOptionalWriteable(capabilities);
        out.writeOptionalWriteable(clientInfo);
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
        if (clientInfo != null) {
            builder.field(CLIENT_INFO.getPreferredName(), clientInfo);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
