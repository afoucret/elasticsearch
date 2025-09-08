/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.core;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpCancelledNotificationTests extends McpSerializationTestCase<
    org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification> {

    @Override
    protected Writeable.Reader<org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification> instanceReader() {
        return McpCancelledNotification::new;
    }

    @Override
    protected org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification createTestInstance() {
        return randomCancelledNotification();
    }

    @Override
    protected org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification doParseInstance(XContentParser parser) throws IOException {
        return org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification.PARSER.parse(parser, null);
    }

    @Override
    protected org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification mutateInstance(
        org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification instance
    ) {
        switch (randomInt(2)) {
            case 0:
                return new org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification(
                    randomValueOtherThan(instance.requestId(), () -> randomFrom(randomAlphaOfLength(10), randomInt())),
                    instance.reason(),
                    instance.meta()
                );
            case 1:
                return new org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification(
                    instance.requestId(),
                    randomValueOtherThan(instance.reason(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 2:
                return new org.elasticsearch.xpack.mcp.schema.core.McpCancelledNotification(
                    instance.requestId(),
                    instance.reason(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
