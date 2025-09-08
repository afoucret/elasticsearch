/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.root;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class McpListRootsResultTests extends McpSerializationTestCase<McpListRootsResult> {

    @Override
    protected Writeable.Reader<McpListRootsResult> instanceReader() {
        return McpListRootsResult::new;
    }

    @Override
    protected McpListRootsResult createTestInstance() {
        return randomListRootsResult();
    }

    @Override
    protected McpListRootsResult doParseInstance(XContentParser parser) throws IOException {
        return McpListRootsResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpListRootsResult mutateInstance(McpListRootsResult instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpListRootsResult(
                    randomValueOtherThan(
                        instance.roots(),
                        () -> IntStream.range(0, randomIntBetween(0, 5)).mapToObj(i -> randomRoot()).collect(Collectors.toList())
                    ),
                    instance.nextCursor(),
                    instance.meta()
                );
            case 1:
                return new McpListRootsResult(
                    instance.roots(),
                    randomValueOtherThan(instance.nextCursor(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 2:
                return new McpListRootsResult(
                    instance.roots(),
                    instance.nextCursor(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
