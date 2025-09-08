/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.elicitation;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;

import java.io.IOException;

public enum McpElicitResultAction implements Writeable {
    ACCEPT("accept"),
    DECLINE("decline"),
    CANCEL("cancel");

    private final String value;

    McpElicitResultAction(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(this);
    }

    public static McpElicitResultAction readFrom(StreamInput in) throws IOException {
        return in.readEnum(McpElicitResultAction.class);
    }

    public static McpElicitResultAction fromString(String value) {
        return switch (value) {
            case "accept" -> ACCEPT;
            case "decline" -> DECLINE;
            case "cancel" -> CANCEL;
            default -> throw new IllegalArgumentException("Unknown McpElicitResultAction [" + value + "]");
        };
    }
}
