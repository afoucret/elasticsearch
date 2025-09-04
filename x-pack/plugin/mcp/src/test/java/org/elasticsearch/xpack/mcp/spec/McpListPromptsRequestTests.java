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

public class McpListPromptsRequestTests extends McpSerializationTestCase<McpListPromptsRequest> {

    @Override
    protected Writeable.Reader<McpListPromptsRequest> instanceReader() {
        return McpListPromptsRequest::new;
    }

    @Override
    protected McpListPromptsRequest createTestInstance() {
        return randomListPromptsRequest();
    }

    @Override
    protected McpListPromptsRequest doParseInstance(XContentParser parser) throws IOException {
        return McpListPromptsRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpListPromptsRequest mutateInstance(McpListPromptsRequest instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpListPromptsRequest(
                    randomValueOtherThan(instance.cursor(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.meta()
                );
            case 1:
                return new McpListPromptsRequest(instance.cursor(), randomValueOtherThan(instance.meta(), this::randomGenericMap));
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
