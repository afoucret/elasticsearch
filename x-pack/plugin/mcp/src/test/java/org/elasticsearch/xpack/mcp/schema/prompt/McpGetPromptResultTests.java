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

public class McpGetPromptResultTests extends McpSerializationTestCase<McpGetPromptResult> {

    @Override
    protected Writeable.Reader<McpGetPromptResult> instanceReader() {
        return McpGetPromptResult::new;
    }

    @Override
    protected McpGetPromptResult createTestInstance() {
        return randomGetPromptResult();
    }

    @Override
    protected McpGetPromptResult doParseInstance(XContentParser parser) throws IOException {
        return McpGetPromptResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpGetPromptResult mutateInstance(McpGetPromptResult instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpGetPromptResult(
                    randomValueOtherThan(
                        instance.messages(),
                        () -> IntStream.range(0, randomIntBetween(0, 5)).mapToObj(i -> randomPromptMessage()).collect(Collectors.toList())
                    ),
                    instance.description(),
                    instance.meta()
                );
            case 1:
                return new McpGetPromptResult(
                    instance.messages(),
                    randomValueOtherThan(instance.description(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 2:
                return new McpGetPromptResult(
                    instance.messages(),
                    instance.description(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
