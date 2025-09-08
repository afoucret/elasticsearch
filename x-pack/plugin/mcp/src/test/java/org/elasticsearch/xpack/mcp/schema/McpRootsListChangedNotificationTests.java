/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;

public class McpRootsListChangedNotificationTests extends McpSerializationTestCase<McpRootsListChangedNotification> {

    @Override
    protected Writeable.Reader<McpRootsListChangedNotification> instanceReader() {
        return McpRootsListChangedNotification::new;
    }

    @Override
    protected McpRootsListChangedNotification createTestInstance() {
        return randomRootsListChangedNotification();
    }

    @Override
    protected McpRootsListChangedNotification doParseInstance(XContentParser parser) throws IOException {
        return McpRootsListChangedNotification.PARSER.parse(parser, null);
    }

    @Override
    protected McpRootsListChangedNotification mutateInstance(McpRootsListChangedNotification instance) {
        return new McpRootsListChangedNotification(randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap)));
    }
}
