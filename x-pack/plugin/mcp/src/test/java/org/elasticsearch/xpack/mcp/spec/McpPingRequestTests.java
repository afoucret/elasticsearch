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

public class McpPingRequestTests extends McpSerializationTestCase<McpPingRequest> {

    @Override
    protected Writeable.Reader<McpPingRequest> instanceReader() {
        return McpPingRequest::new;
    }

    @Override
    protected McpPingRequest createTestInstance() {
        return randomPingRequest();
    }

    @Override
    protected McpPingRequest doParseInstance(XContentParser parser) throws IOException {
        return McpPingRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpPingRequest mutateInstance(McpPingRequest instance) {
        return new McpPingRequest(randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap)));
    }
}
