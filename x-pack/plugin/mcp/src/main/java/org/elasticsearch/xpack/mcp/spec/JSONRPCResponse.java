/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record JSONRPCResponse(@NotNull String jsonrpc, @NotNull String id, Map<String, Object> result, JSONRPCError error)
    implements
        JSONRPCMessage {

    public static final String NAME = "jsonrpc_response";

    private static final ParseField JSONRPC_FIELD = new ParseField("jsonrpc");
    private static final ParseField ID_FIELD = new ParseField("id");
    private static final ParseField RESULT_FIELD = new ParseField("result");
    private static final ParseField ERROR_FIELD = new ParseField("error");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<JSONRPCResponse, Void> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new JSONRPCResponse((String) args[0], (String) args[1], (Map<String, Object>) args[2], (JSONRPCError) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), JSONRPC_FIELD);
        PARSER.declareString(constructorArg(), ID_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), RESULT_FIELD);
        PARSER.declareObject(optionalConstructorArg(), JSONRPCError.PARSER, ERROR_FIELD);
    }

    public JSONRPCResponse(StreamInput in) throws IOException {
        this(in.readString(), in.readString(), in.readOptional(StreamInput::readGenericMap), in.readOptionalWriteable(JSONRPCError::new));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(jsonrpc);
        out.writeString(id);
        out.writeOptional(StreamOutput::writeGenericMap, result);
        out.writeOptionalWriteable(error);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(JSONRPC_FIELD.getPreferredName(), jsonrpc);
        builder.field(ID_FIELD.getPreferredName(), id);
        if (result != null) {
            builder.field(RESULT_FIELD.getPreferredName(), result);
        }
        if (error != null) {
            builder.field(ERROR_FIELD.getPreferredName(), error);
        }
        builder.endObject();
        return builder;
    }
}
