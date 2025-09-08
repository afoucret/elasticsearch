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

public class McpListToolsRequestTests extends McpSerializationTestCase<McpListToolsRequest> {

    @Override
    protected Writeable.Reader<McpListToolsRequest> instanceReader() {
        return McpListToolsRequest::new;
    }

    @Override
    protected McpListToolsRequest createTestInstance() {
        return randomListToolsRequest();
    }

    @Override
    protected McpListToolsRequest doParseInstance(XContentParser parser) throws IOException {
        return McpListToolsRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpListToolsRequest mutateInstance(McpListToolsRequest instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpListToolsRequest(
                    randomValueOtherThan(instance.cursor(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 1:
                return new McpListToolsRequest(instance.cursor(), randomValueOtherThan(instance.meta(), this::randomGenericMap));
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
