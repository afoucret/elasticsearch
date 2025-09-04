/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;

public class McpLoggingMessageNotificationTests extends McpSerializationTestCase<McpLoggingMessageNotification> {

    @Override
    protected Writeable.Reader<McpLoggingMessageNotification> instanceReader() {
        return McpLoggingMessageNotification::new;
    }

    @Override
    protected McpLoggingMessageNotification createTestInstance() {
        return randomLoggingMessageNotification();
    }

    @Override
    protected McpLoggingMessageNotification doParseInstance(XContentParser parser) throws IOException {
        return McpLoggingMessageNotification.PARSER.parse(parser, null);
    }

    @Override
    protected McpLoggingMessageNotification mutateInstance(McpLoggingMessageNotification instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpLoggingMessageNotification(
                    randomValueOtherThan(instance.level(), () -> randomFrom(McpLoggingLevel.values())),
                    instance.data(),
                    instance.logger(),
                    instance.meta()
                );
            case 1:
                return new McpLoggingMessageNotification(
                    instance.level(),
                    randomValueOtherThan(instance.data(), this::randomGenericMap),
                    instance.logger(),
                    instance.meta()
                );
            case 2:
                return new McpLoggingMessageNotification(
                    instance.level(),
                    instance.data(),
                    randomValueOtherThan(instance.logger(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 3:
                return new McpLoggingMessageNotification(
                    instance.level(),
                    instance.data(),
                    instance.logger(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
