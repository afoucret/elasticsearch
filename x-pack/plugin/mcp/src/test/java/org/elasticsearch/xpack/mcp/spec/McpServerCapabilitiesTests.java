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

public class McpServerCapabilitiesTests extends McpSerializationTestCase<McpServerCapabilities> {

    @Override
    protected Writeable.Reader<McpServerCapabilities> instanceReader() {
        return McpServerCapabilities::new;
    }

    @Override
    protected McpServerCapabilities createTestInstance() {
        return randomServerCapabilities();
    }

    @Override
    protected McpServerCapabilities doParseInstance(XContentParser parser) throws IOException {
        return McpServerCapabilities.PARSER.parse(parser, null);
    }

    @Override
    protected McpServerCapabilities mutateInstance(McpServerCapabilities instance) {
        switch (randomInt(5)) {
            case 0:
                return new McpServerCapabilities(
                    instance.completions() == null ? new McpServerCapabilities.CompletionCapabilities() : null,
                    instance.experimental(),
                    instance.logging(),
                    instance.prompts(),
                    instance.resources(),
                    instance.tools()
                );
            case 1:
                return new McpServerCapabilities(
                    new McpServerCapabilities.CompletionCapabilities(),
                    randomValueOtherThan(instance.experimental(), () -> mayBeNull(this::randomGenericMap)),
                    instance.logging(),
                    instance.prompts(),
                    instance.resources(),
                    instance.tools()
                );
            case 2:
                return new McpServerCapabilities(
                    instance.completions(),
                    instance.experimental(),
                    instance.logging() == null ? new McpServerCapabilities.LoggingCapabilities() : null,
                    instance.prompts(),
                    instance.resources(),
                    instance.tools()
                );
            case 3:
                return new McpServerCapabilities(
                    instance.completions(),
                    instance.experimental(),
                    instance.logging(),
                    randomValueOtherThan(
                        instance.prompts(),
                        () -> mayBeNull(() -> new McpServerCapabilities.PromptCapabilities(randomOptionalBoolean()))
                    ),
                    instance.resources(),
                    instance.tools()
                );
            case 4:
                return new McpServerCapabilities(
                    instance.completions(),
                    instance.experimental(),
                    instance.logging(),
                    instance.prompts(),
                    randomValueOtherThan(
                        instance.resources(),
                        () -> mayBeNull(
                            () -> new McpServerCapabilities.ResourceCapabilities(randomOptionalBoolean(), randomOptionalBoolean())
                        )
                    ),
                    instance.tools()
                );
            case 5:
                return new McpServerCapabilities(
                    instance.completions(),
                    instance.experimental(),
                    instance.logging(),
                    instance.prompts(),
                    instance.resources(),
                    randomValueOtherThan(
                        instance.tools(),
                        () -> mayBeNull(() -> new McpServerCapabilities.ToolCapabilities(randomOptionalBoolean()))
                    )
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
