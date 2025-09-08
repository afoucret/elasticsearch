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

public class McpResourcesUpdatedNotificationTests extends McpSerializationTestCase<McpResourcesUpdatedNotification> {

    @Override
    protected Writeable.Reader<McpResourcesUpdatedNotification> instanceReader() {
        return McpResourcesUpdatedNotification::new;
    }

    @Override
    protected McpResourcesUpdatedNotification createTestInstance() {
        return randomResourcesUpdatedNotification();
    }

    @Override
    protected McpResourcesUpdatedNotification doParseInstance(XContentParser parser) throws IOException {
        return McpResourcesUpdatedNotification.PARSER.parse(parser, null);
    }

    @Override
    protected McpResourcesUpdatedNotification mutateInstance(McpResourcesUpdatedNotification instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpResourcesUpdatedNotification(
                    randomValueOtherThan(instance.uri(), () -> randomAlphaOfLength(10)),
                    instance.meta()
                );
            case 1:
                return new McpResourcesUpdatedNotification(
                    instance.uri(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
