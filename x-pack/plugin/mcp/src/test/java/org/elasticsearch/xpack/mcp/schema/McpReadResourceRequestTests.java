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

public class McpReadResourceRequestTests extends McpSerializationTestCase<McpReadResourceRequest> {

    @Override
    protected Writeable.Reader<McpReadResourceRequest> instanceReader() {
        return McpReadResourceRequest::new;
    }

    @Override
    protected McpReadResourceRequest createTestInstance() {
        return randomReadResourceRequest();
    }

    @Override
    protected McpReadResourceRequest doParseInstance(XContentParser parser) throws IOException {
        return McpReadResourceRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpReadResourceRequest mutateInstance(McpReadResourceRequest instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpReadResourceRequest(randomValueOtherThan(instance.uri(), () -> randomAlphaOfLength(10)), instance.meta());
            case 1:
                return new McpReadResourceRequest(
                    instance.uri(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
