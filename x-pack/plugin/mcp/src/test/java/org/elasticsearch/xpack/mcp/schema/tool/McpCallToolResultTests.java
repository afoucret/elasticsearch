/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.tool;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpCallToolResultTests extends McpSerializationTestCase<McpCallToolResult> {

    @Override
    protected Writeable.Reader<McpCallToolResult> instanceReader() {
        return McpCallToolResult::new;
    }

    @Override
    protected McpCallToolResult createTestInstance() {
        return randomCallToolResult();
    }

    @Override
    protected McpCallToolResult doParseInstance(XContentParser parser) throws IOException {
        return McpCallToolResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpCallToolResult mutateInstance(McpCallToolResult instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpCallToolResult(
                    randomValueOtherThan(instance.content(), () -> randomList(10, this::randomContent)),
                    instance.isError(),
                    instance.structuredContent(),
                    instance.meta()
                );
            case 1:
                return new McpCallToolResult(
                    instance.content(),
                    randomValueOtherThan(instance.isError(), ESTestCase::randomOptionalBoolean),
                    instance.structuredContent(),
                    instance.meta()
                );
            case 2:
                return new McpCallToolResult(
                    instance.content(),
                    instance.isError(),
                    randomValueOtherThan(instance.structuredContent(), () -> mayBeNull(this::randomGenericMap)),
                    instance.meta()
                );
            case 3:
                return new McpCallToolResult(
                    instance.content(),
                    instance.isError(),
                    instance.structuredContent(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
