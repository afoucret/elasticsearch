/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.prompt;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpPromptsListChangedNotificationTests extends McpSerializationTestCase<McpPromptsListChangedNotification> {

    @Override
    protected Writeable.Reader<McpPromptsListChangedNotification> instanceReader() {
        return McpPromptsListChangedNotification::new;
    }

    @Override
    protected McpPromptsListChangedNotification createTestInstance() {
        return randomPromptsListChangedNotification();
    }

    @Override
    protected McpPromptsListChangedNotification doParseInstance(XContentParser parser) throws IOException {
        return McpPromptsListChangedNotification.PARSER.parse(parser, null);
    }

    @Override
    protected McpPromptsListChangedNotification mutateInstance(McpPromptsListChangedNotification instance) {
        return new McpPromptsListChangedNotification(randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap)));
    }
}
