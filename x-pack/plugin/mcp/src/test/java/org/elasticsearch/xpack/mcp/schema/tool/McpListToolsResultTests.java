/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.tool;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class McpListToolsResultTests extends McpSerializationTestCase<McpListToolsResult> {

    @Override
    protected Writeable.Reader<McpListToolsResult> instanceReader() {
        return McpListToolsResult::new;
    }

    @Override
    protected McpListToolsResult createTestInstance() {
        return randomListToolsResult();
    }

    @Override
    protected McpListToolsResult doParseInstance(XContentParser parser) throws IOException {
        return McpListToolsResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpListToolsResult mutateInstance(McpListToolsResult instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpListToolsResult(
                    randomValueOtherThan(
                        instance.tools(),
                        () -> IntStream.range(0, randomIntBetween(0, 5)).mapToObj(i -> randomTool()).collect(Collectors.toList())
                    ),
                    instance.nextCursor(),
                    instance.meta()
                );
            case 1:
                return new McpListToolsResult(
                    instance.tools(),
                    randomValueOtherThan(instance.nextCursor(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 2:
                return new McpListToolsResult(
                    instance.tools(),
                    instance.nextCursor(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
