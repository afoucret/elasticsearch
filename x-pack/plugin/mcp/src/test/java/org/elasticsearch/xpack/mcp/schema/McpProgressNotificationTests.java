/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;

public class McpProgressNotificationTests extends McpSerializationTestCase<McpProgressNotification> {

    @Override
    protected Writeable.Reader<McpProgressNotification> instanceReader() {
        return McpProgressNotification::new;
    }

    @Override
    protected McpProgressNotification createTestInstance() {
        return this.randomProgressNotification();
    }

    @Override
    protected McpProgressNotification doParseInstance(XContentParser parser) throws IOException {
        return McpProgressNotification.PARSER.parse(parser, null);
    }

    @Override
    protected McpProgressNotification mutateInstance(McpProgressNotification instance) {
        switch (randomInt(4)) {
            case 0:
                return new McpProgressNotification(
                    randomValueOtherThan(instance.progressToken(), () -> randomFrom(randomAlphaOfLength(10), randomInt())),
                    instance.progress(),
                    instance.total(),
                    instance.message(),
                    instance.meta()
                );
            case 1:
                return new McpProgressNotification(
                    instance.progressToken(),
                    randomValueOtherThan(instance.progress(), ESTestCase::randomDouble),
                    instance.total(),
                    instance.message(),
                    instance.meta()
                );
            case 2:
                return new McpProgressNotification(
                    instance.progressToken(),
                    instance.progress(),
                    randomValueOtherThan(instance.total(), ESTestCase::randomDouble),
                    instance.message(),
                    instance.meta()
                );
            case 3:
                return new McpProgressNotification(
                    instance.progressToken(),
                    instance.progress(),
                    instance.total(),
                    randomValueOtherThan(instance.message(), () -> randomAlphaOfLengthOrNull(20)),
                    instance.meta()
                );
            case 4:
                return new McpProgressNotification(
                    instance.progressToken(),
                    instance.progress(),
                    instance.total(),
                    instance.message(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
