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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class McpReadResourceResultTests extends McpSerializationTestCase<McpReadResourceResult> {

    @Override
    protected Writeable.Reader<McpReadResourceResult> instanceReader() {
        return McpReadResourceResult::new;
    }

    @Override
    protected McpReadResourceResult createTestInstance() {
        return randomReadResourceResult();
    }

    @Override
    protected McpReadResourceResult doParseInstance(XContentParser parser) throws IOException {
        return McpReadResourceResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpReadResourceResult mutateInstance(McpReadResourceResult instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpReadResourceResult(
                    randomValueOtherThan(
                        instance.contents(),
                        () -> IntStream.range(0, randomIntBetween(0, 5)).mapToObj(i -> randomResourceContent()).collect(Collectors.toList())
                    ),
                    instance.meta()
                );
            case 1:
                return new McpReadResourceResult(
                    instance.contents(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }

}
