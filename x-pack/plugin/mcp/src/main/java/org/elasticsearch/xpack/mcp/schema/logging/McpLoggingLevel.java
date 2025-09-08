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

public enum McpLoggingLevel implements Writeable {
    DEBUG("debug"),
    INFO("info"),
    NOTICE("notice"),
    WARNING("warning"),
    ERROR("error"),
    CRITICAL("critical"),
    ALERT("alert"),
    EMERGENCY("emergency");

    private final String value;

    McpLoggingLevel(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(this);
    }

    public static McpLoggingLevel readFrom(StreamInput in) throws IOException {
        return in.readEnum(McpLoggingLevel.class);
    }

    public static McpLoggingLevel fromString(String value) {
        for (McpLoggingLevel level : values()) {
            if (level.value.equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown McpLoggingLevel [" + value + "]");
    }
}
