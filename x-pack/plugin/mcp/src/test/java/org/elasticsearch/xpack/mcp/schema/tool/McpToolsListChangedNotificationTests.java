/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.tool;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpToolsListChangedNotificationTests extends McpSerializationTestCase<McpToolsListChangedNotification> {

    @Override
    protected Writeable.Reader<McpToolsListChangedNotification> instanceReader() {
        return McpToolsListChangedNotification::new;
    }

    @Override
    protected McpToolsListChangedNotification createTestInstance() {
        return randomToolsListChangedNotification();
    }

    @Override
    protected McpToolsListChangedNotification doParseInstance(XContentParser parser) throws IOException {
        return McpToolsListChangedNotification.PARSER.parse(parser, null);
    }

    @Override
    protected McpToolsListChangedNotification mutateInstance(McpToolsListChangedNotification instance) {
        return new McpToolsListChangedNotification(randomValueOtherThan(instance.meta(), this::randomGenericMap));
    }
}
