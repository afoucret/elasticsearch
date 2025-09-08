/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.handshake;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpInitializedNotificationTests extends McpSerializationTestCase<McpInitializedNotification> {

    @Override
    protected Writeable.Reader<McpInitializedNotification> instanceReader() {
        return McpInitializedNotification::new;
    }

    @Override
    protected McpInitializedNotification createTestInstance() {
        return randomInitializedNotification();
    }

    @Override
    protected McpInitializedNotification doParseInstance(XContentParser parser) throws IOException {
        return McpInitializedNotification.PARSER.parse(parser, null);
    }

    @Override
    protected McpInitializedNotification mutateInstance(McpInitializedNotification instance) {
        return new McpInitializedNotification(randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap)));
    }
}
