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

public class McpSubscribeRequestTests extends McpSerializationTestCase<McpSubscribeRequest> {

    @Override
    protected Writeable.Reader<McpSubscribeRequest> instanceReader() {
        return McpSubscribeRequest::new;
    }

    @Override
    protected McpSubscribeRequest createTestInstance() {
        return randomSubscribeRequest();
    }

    @Override
    protected McpSubscribeRequest doParseInstance(XContentParser parser) throws IOException {
        return McpSubscribeRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpSubscribeRequest mutateInstance(McpSubscribeRequest instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpSubscribeRequest(randomValueOtherThan(instance.uri(), () -> randomAlphaOfLength(10)), instance.meta());
            case 1:
                return new McpSubscribeRequest(
                    instance.uri(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
