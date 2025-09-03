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
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record JSONRPCError(@NotNull int code, @NotNull String message, Map<String, Object> data)
    implements
        NamedWriteable,
        ToXContentObject {

    public static final String NAME = "jsonrpc_error";

    private static final ParseField CODE_FIELD = new ParseField("code");
    private static final ParseField MESSAGE_FIELD = new ParseField("message");
    private static final ParseField DATA_FIELD = new ParseField("data");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<JSONRPCError, Void> PARSER = new ConstructingObjectParser<>(
        "jsonrpc_error",
        args -> new JSONRPCError((int) args[0], (String) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareInt(constructorArg(), CODE_FIELD);
        PARSER.declareString(constructorArg(), MESSAGE_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), DATA_FIELD);
    }

    public JSONRPCError(StreamInput in) throws IOException {
        this(in.readInt(), in.readString(), in.readMap(StreamInput::readString, StreamInput::readGenericValue));
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeInt(code);
        out.writeString(message);
        out.writeMap(data, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(CODE_FIELD.getPreferredName(), code);
        builder.field(MESSAGE_FIELD.getPreferredName(), message);
        if (data != null) {
            builder.field(DATA_FIELD.getPreferredName(), data);
        }
        builder.endObject();
        return builder;
    }
}
