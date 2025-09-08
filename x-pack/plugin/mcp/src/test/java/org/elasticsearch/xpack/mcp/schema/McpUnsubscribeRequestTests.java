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

public class McpUnsubscribeRequestTests extends McpSerializationTestCase<McpUnsubscribeRequest> {

    @Override
    protected Writeable.Reader<McpUnsubscribeRequest> instanceReader() {
        return McpUnsubscribeRequest::new;
    }

    @Override
    protected McpUnsubscribeRequest createTestInstance() {
        return randomUnsubscribeRequest();
    }

    @Override
    protected McpUnsubscribeRequest doParseInstance(XContentParser parser) throws IOException {
        return McpUnsubscribeRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpUnsubscribeRequest mutateInstance(McpUnsubscribeRequest instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpUnsubscribeRequest(randomValueOtherThan(instance.uri(), () -> randomAlphaOfLength(10)), instance.meta());
            case 1:
                return new McpUnsubscribeRequest(
                    instance.uri(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
