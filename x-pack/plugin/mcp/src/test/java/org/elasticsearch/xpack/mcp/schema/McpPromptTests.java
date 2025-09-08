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

public class McpPromptTests extends McpSerializationTestCase<McpPrompt> {

    @Override
    protected Writeable.Reader<McpPrompt> instanceReader() {
        return McpPrompt::new;
    }

    @Override
    protected McpPrompt createTestInstance() {
        return randomPrompt();
    }

    @Override
    protected McpPrompt doParseInstance(XContentParser parser) throws IOException {
        return McpPrompt.PARSER.parse(parser, null);
    }

    @Override
    protected McpPrompt mutateInstance(McpPrompt instance) {
        switch (randomInt(4)) {
            case 0:
                return new McpPrompt(
                    randomValueOtherThan(instance.name(), () -> randomAlphaOfLength(10)),
                    instance.title(),
                    instance.description(),
                    instance.arguments(),
                    instance.meta()
                );
            case 1:
                return new McpPrompt(
                    instance.name(),
                    randomValueOtherThan(instance.title(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.description(),
                    instance.arguments(),
                    instance.meta()
                );
            case 2:
                return new McpPrompt(
                    instance.name(),
                    instance.title(),
                    randomValueOtherThan(instance.description(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.arguments(),
                    instance.meta()
                );
            case 3:
                return new McpPrompt(
                    instance.name(),
                    instance.title(),
                    instance.description(),
                    randomValueOtherThan(instance.arguments(), () -> mayBeNull(() -> randomList(5, this::randomPromptArgument))),
                    instance.meta()
                );
            case 4:
                return new McpPrompt(
                    instance.name(),
                    instance.title(),
                    instance.description(),
                    instance.arguments(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
