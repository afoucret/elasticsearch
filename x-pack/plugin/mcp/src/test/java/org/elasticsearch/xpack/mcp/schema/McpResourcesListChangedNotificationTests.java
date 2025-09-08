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

public class McpResourcesListChangedNotificationTests extends McpSerializationTestCase<McpResourcesListChangedNotification> {

    @Override
    protected Writeable.Reader<McpResourcesListChangedNotification> instanceReader() {
        return McpResourcesListChangedNotification::new;
    }

    @Override
    protected McpResourcesListChangedNotification createTestInstance() {
        return randomResourcesListChangedNotification();
    }

    @Override
    protected McpResourcesListChangedNotification doParseInstance(XContentParser parser) throws IOException {
        return McpResourcesListChangedNotification.PARSER.parse(parser, null);
    }

    @Override
    protected McpResourcesListChangedNotification mutateInstance(McpResourcesListChangedNotification instance) {
        return new McpResourcesListChangedNotification(randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap)));
    }
}
