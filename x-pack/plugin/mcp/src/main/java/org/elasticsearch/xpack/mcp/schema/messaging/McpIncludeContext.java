/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;

import java.io.IOException;

public enum McpIncludeContext implements Writeable {
    NONE("none"),
    THIS_SERVER("thisServer"),
    ALL_SERVERS("allServers");

    private final String value;

    McpIncludeContext(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(this);
    }

    public static McpIncludeContext readFrom(StreamInput in) throws IOException {
        return in.readEnum(McpIncludeContext.class);
    }

    public static McpIncludeContext fromString(String value) {
        return switch (value) {
            case "none" -> NONE;
            case "thisServer" -> THIS_SERVER;
            case "allServers" -> ALL_SERVERS;
            default -> throw new IllegalArgumentException("Unknown McpIncludeContext [" + value + "]");
        };
    }
}
