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

public class McpRootTests extends McpSerializationTestCase<McpRoot> {

    @Override
    protected Writeable.Reader<McpRoot> instanceReader() {
        return McpRoot::new;
    }

    @Override
    protected McpRoot createTestInstance() {
        return randomRoot();
    }

    @Override
    protected McpRoot doParseInstance(XContentParser parser) throws IOException {
        return McpRoot.PARSER.parse(parser, null);
    }

    @Override
    protected McpRoot mutateInstance(McpRoot instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpRoot(randomValueOtherThan(instance.uri(), () -> randomAlphaOfLength(10)), instance.name(), instance.meta());
            case 1:
                return new McpRoot(
                    instance.uri(),
                    randomValueOtherThan(instance.name(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 2:
                return new McpRoot(
                    instance.uri(),
                    instance.name(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
