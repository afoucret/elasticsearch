/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record JSONRPCRequest(String jsonrpc, String method, String id, Map<String, Object> params) implements JSONRPCMessage {

    private static final ParseField JSONRPC = new ParseField("jsonrpc");
    private static final ParseField METHOD = new ParseField("method");
    private static final ParseField ID = new ParseField("id");
    private static final ParseField PARAMS = new ParseField("params");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<JSONRPCRequest, Void> PARSER = new ConstructingObjectParser<>(
        "jsonrpc_request",
        args -> new JSONRPCRequest((String) args[0], (String) args[1], (String) args[2], (Map<String, Object>) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), JSONRPC);
        PARSER.declareString(constructorArg(), METHOD);
        PARSER.declareString(constructorArg(), ID);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), PARAMS);
    }

    public JSONRPCRequest(StreamInput in) throws IOException {
        this(in.readString(), in.readString(), in.readString(), in.readMap(StreamInput::readString, StreamInput::readGenericValue));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(jsonrpc);
        out.writeString(method);
        out.writeString(id);
        out.writeMap(params, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(JSONRPC.getPreferredName(), jsonrpc);
        builder.field(METHOD.getPreferredName(), method);
        builder.field(ID.getPreferredName(), id);
        if (params != null) {
            builder.field(PARAMS.getPreferredName(), this.params);
        }
        builder.endObject();
        return builder;
    }
}
