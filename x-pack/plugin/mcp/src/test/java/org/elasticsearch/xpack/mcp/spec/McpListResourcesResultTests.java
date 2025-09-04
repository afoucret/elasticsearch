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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class McpListResourcesResultTests extends McpSerializationTestCase<McpListResourcesResult> {

    @Override
    protected Writeable.Reader<McpListResourcesResult> instanceReader() {
        return McpListResourcesResult::new;
    }

    @Override
    protected McpListResourcesResult createTestInstance() {
        return randomListResourcesResult();
    }

    @Override
    protected McpListResourcesResult doParseInstance(XContentParser parser) throws IOException {
        return McpListResourcesResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpListResourcesResult mutateInstance(McpListResourcesResult instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpListResourcesResult(
                    randomValueOtherThan(
                        instance.resources(),
                        () -> IntStream.range(0, randomIntBetween(0, 5)).mapToObj(i -> randomResource()).collect(Collectors.toList())
                    ),
                    instance.nextCursor(),
                    instance.meta()
                );
            case 1:
                return new McpListResourcesResult(
                    instance.resources(),
                    randomValueOtherThan(instance.nextCursor(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 2:
                return new McpListResourcesResult(
                    instance.resources(),
                    instance.nextCursor(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
