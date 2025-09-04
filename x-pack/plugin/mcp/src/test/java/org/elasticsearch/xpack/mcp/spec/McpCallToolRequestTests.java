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

public class McpCallToolRequestTests extends McpSerializationTestCase<McpCallToolRequest> {

    @Override
    protected Writeable.Reader<McpCallToolRequest> instanceReader() {
        return McpCallToolRequest::new;
    }

    @Override
    protected McpCallToolRequest createTestInstance() {
        return randomCallToolRequest();
    }

    @Override
    protected McpCallToolRequest doParseInstance(XContentParser parser) throws IOException {
        return McpCallToolRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpCallToolRequest mutateInstance(McpCallToolRequest instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpCallToolRequest(
                    randomValueOtherThan(instance.name(), () -> randomAlphaOfLength(10)),
                    instance.arguments(),
                    instance.meta()
                );
            case 1:
                return new McpCallToolRequest(
                    instance.name(),
                    randomValueOtherThan(instance.arguments(), () -> mayBeNull(this::randomGenericMap)),
                    instance.meta()
                );
            case 2:
                return new McpCallToolRequest(
                    instance.name(),
                    instance.arguments(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
