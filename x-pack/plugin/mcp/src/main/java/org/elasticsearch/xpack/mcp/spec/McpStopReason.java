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

import java.io.IOException;

public enum McpStopReason implements Writeable {
    END_TURN("endTurn"),
    STOP_SEQUENCE("stopSequence"),
    MAX_TOKENS("maxTokens");

    private final String value;

    McpStopReason(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(this);
    }

    public static McpStopReason readFrom(StreamInput in) throws IOException {
        return in.readEnum(McpStopReason.class);
    }

    public static McpStopReason fromString(String value) {
        return switch (value) {
            case "endTurn" -> END_TURN;
            case "stopSequence" -> STOP_SEQUENCE;
            case "maxTokens" -> MAX_TOKENS;
            default -> throw new IllegalArgumentException("Unknown McpStopReason [" + value + "]");
        };
    }
}
