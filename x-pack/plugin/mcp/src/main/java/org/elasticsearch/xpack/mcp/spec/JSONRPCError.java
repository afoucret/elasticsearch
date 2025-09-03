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

public record JSONRPCError(int code, String message, Map<String, Object> data) implements Writeable, ToXContentObject {

    private static final ParseField CODE = new ParseField("code");
    private static final ParseField MESSAGE = new ParseField("message");
    private static final ParseField DATA = new ParseField("data");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<JSONRPCError, Void> PARSER = new ConstructingObjectParser<>(
        "jsonrpc_error",
        args -> new JSONRPCError((int) args[0], (String) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareInt(constructorArg(), CODE);
        PARSER.declareString(constructorArg(), MESSAGE);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), DATA);
    }

    public JSONRPCError(StreamInput in) throws IOException {
        this(in.readInt(), in.readString(), in.readMap(StreamInput::readString, StreamInput::readGenericValue));
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
        builder.field(CODE.getPreferredName(), code);
        builder.field(MESSAGE.getPreferredName(), message);
        if (data != null) {
            builder.field(DATA.getPreferredName(), data);
        }
        builder.endObject();
        return builder;
    }
}
