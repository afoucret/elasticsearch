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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class McpListPromptsResultTests extends McpSerializationTestCase<McpListPromptsResult> {

    @Override
    protected Writeable.Reader<McpListPromptsResult> instanceReader() {
        return McpListPromptsResult::new;
    }

    @Override
    protected McpListPromptsResult createTestInstance() {
        return randomListPromptsResult();
    }

    @Override
    protected McpListPromptsResult doParseInstance(XContentParser parser) throws IOException {
        return McpListPromptsResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpListPromptsResult mutateInstance(McpListPromptsResult instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpListPromptsResult(
                    randomValueOtherThan(
                        instance.prompts(),
                        () -> IntStream.range(0, randomIntBetween(0, 5)).mapToObj(i -> randomPrompt()).collect(Collectors.toList())
                    ),
                    instance.nextCursor(),
                    instance.meta()
                );
            case 1:
                return new McpListPromptsResult(
                    instance.prompts(),
                    randomValueOtherThan(instance.nextCursor(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 2:
                return new McpListPromptsResult(
                    instance.prompts(),
                    instance.nextCursor(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
