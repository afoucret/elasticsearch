/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;

public class McpPromptArgumentTests extends McpSerializationTestCase<McpPromptArgument> {

    @Override
    protected Writeable.Reader<McpPromptArgument> instanceReader() {
        return McpPromptArgument::new;
    }

    @Override
    protected McpPromptArgument createTestInstance() {
        return randomPromptArgument();
    }

    @Override
    protected McpPromptArgument doParseInstance(XContentParser parser) throws IOException {
        return McpPromptArgument.PARSER.parse(parser, null);
    }

    @Override
    protected McpPromptArgument mutateInstance(McpPromptArgument instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpPromptArgument(
                    randomValueOtherThan(instance.name(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.title(),
                    instance.description(),
                    instance.required()
                );
            case 1:
                return new McpPromptArgument(
                    instance.name(),
                    randomValueOtherThan(instance.title(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.description(),
                    instance.required()
                );
            case 2:
                return new McpPromptArgument(
                    instance.name(),
                    instance.title(),
                    randomValueOtherThan(instance.description(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.required()
                );
            case 3:
                return new McpPromptArgument(
                    instance.name(),
                    instance.title(),
                    instance.description(),
                    randomValueOtherThan(instance.required(), ESTestCase::randomOptionalBoolean)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
