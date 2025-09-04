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

public class McpListResourceTemplatesRequestTests extends McpSerializationTestCase<McpListResourceTemplatesRequest> {

    @Override
    protected Writeable.Reader<McpListResourceTemplatesRequest> instanceReader() {
        return McpListResourceTemplatesRequest::new;
    }

    @Override
    protected McpListResourceTemplatesRequest createTestInstance() {
        return randomListResourceTemplatesRequest();
    }

    @Override
    protected McpListResourceTemplatesRequest doParseInstance(XContentParser parser) throws IOException {
        return McpListResourceTemplatesRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpListResourceTemplatesRequest mutateInstance(McpListResourceTemplatesRequest instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpListResourceTemplatesRequest(
                    randomValueOtherThan(instance.cursor(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 1:
                return new McpListResourceTemplatesRequest(
                    instance.cursor(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
