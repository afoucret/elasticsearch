/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.resource;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpListResourcesRequestTests extends McpSerializationTestCase<McpListResourcesRequest> {

    @Override
    protected Writeable.Reader<McpListResourcesRequest> instanceReader() {
        return McpListResourcesRequest::new;
    }

    @Override
    protected McpListResourcesRequest createTestInstance() {
        return randomListResourcesRequest();
    }

    @Override
    protected McpListResourcesRequest doParseInstance(XContentParser parser) throws IOException {
        return McpListResourcesRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpListResourcesRequest mutateInstance(McpListResourcesRequest instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpListResourcesRequest(
                    randomValueOtherThan(instance.cursor(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 1:
                return new McpListResourcesRequest(instance.cursor(), randomValueOtherThan(instance.meta(), this::randomGenericMap));
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
