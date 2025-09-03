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

public record JSONRPCResponse(String jsonrpc, String id, Map<String, Object> result, JSONRPCError error) implements JSONRPCMessage {

    private static final ParseField JSONRPC = new ParseField("jsonrpc");
    private static final ParseField ID = new ParseField("id");
    private static final ParseField RESULT = new ParseField("result");
    private static final ParseField ERROR = new ParseField("error");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<JSONRPCResponse, Void> PARSER = new ConstructingObjectParser<>(
        "jsonrpc_response",
        args -> new JSONRPCResponse((String) args[0], (String) args[1], (Map<String, Object>) args[2], (JSONRPCError) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), JSONRPC);
        PARSER.declareString(constructorArg(), ID);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), RESULT);
        PARSER.declareObject(optionalConstructorArg(), JSONRPCError.PARSER, ERROR);
    }

    public JSONRPCResponse(StreamInput in) throws IOException {
        this(
            in.readString(),
            in.readString(),
            in.readMap(StreamInput::readString, StreamInput::readGenericValue),
            in.readOptionalWriteable(JSONRPCError::new)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(jsonrpc);
        out.writeString(id);
        out.writeMap(result, StreamOutput::writeString, StreamOutput::writeGenericValue);
        out.writeOptionalWriteable(error);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(JSONRPC.getPreferredName(), jsonrpc);
        builder.field(ID.getPreferredName(), id);
        if (result != null) {
            builder.field(RESULT.getPreferredName(), result);
        }
        if (error != null) {
            builder.field(ERROR.getPreferredName(), error);
        }
        builder.endObject();
        return builder;
    }
}
