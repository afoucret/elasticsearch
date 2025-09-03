/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ToXContentObject;

import java.io.IOException;
import java.util.Map;

public sealed interface McpResourceContent extends NamedWriteable, ToXContentObject permits McpTextResourceContents,
    McpBlobResourceContents {
    String uri();

    String mimeType();

    Map<String, Object> meta();

    void writeTo(StreamOutput out) throws IOException;

    static McpResourceContent readFrom(StreamInput in) throws IOException {
        return in.readNamedWriteable(McpResourceContent.class);
    }
}
