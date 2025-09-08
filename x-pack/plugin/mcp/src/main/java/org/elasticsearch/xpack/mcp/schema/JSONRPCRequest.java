/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record JSONRPCRequest(@NotNull String jsonrpc, @NotNull String method, @NotNull Object id, Map<String, Object> params)
    implements
        JSONRPCMessage {

    public static final String NAME = "jsonrpc_request";

    private static final ParseField JSONRPC_FIELD = new ParseField("jsonrpc");
    private static final ParseField METHOD_FIELD = new ParseField("method");
    private static final ParseField ID_FIELD = new ParseField("id");
    private static final ParseField PARAMS_FIELD = new ParseField("params");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<JSONRPCRequest, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new JSONRPCRequest((String) args[0], (String) args[1], args[2], (Map<String, Object>) args[3])
    );

    static {
        PARSER.declareString(constructorArg(), JSONRPC_FIELD);
        PARSER.declareString(constructorArg(), METHOD_FIELD);
        PARSER.declareField(constructorArg(), (p, c) -> switch (p.currentToken()) {
            case VALUE_NUMBER -> p.numberValue();
            case VALUE_STRING -> p.text();
            default -> throw new ParsingException(
                p.getTokenLocation(),
                "Unsupported value type [" + p.currentToken() + "] for field [" + ID_FIELD.getPreferredName() + "]"
            );
        }, ID_FIELD, ObjectParser.ValueType.VALUE);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), PARAMS_FIELD);
    }

    public JSONRPCRequest(StreamInput in) throws IOException {
        this(in.readString(), in.readString(), in.readGenericValue(), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(jsonrpc);
        out.writeString(method);
        out.writeGenericValue(id);
        out.writeOptional(StreamOutput::writeGenericMap, params);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(JSONRPC_FIELD.getPreferredName(), jsonrpc);
        builder.field(METHOD_FIELD.getPreferredName(), method);
        builder.field(ID_FIELD.getPreferredName(), id);
        if (params != null) {
            builder.field(PARAMS_FIELD.getPreferredName(), this.params);
        }
        builder.endObject();
        return builder;
    }
}
