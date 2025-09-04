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

public class McpListRootsRequestTests extends McpSerializationTestCase<McpListRootsRequest> {

    @Override
    protected Writeable.Reader<McpListRootsRequest> instanceReader() {
        return McpListRootsRequest::new;
    }

    @Override
    protected McpListRootsRequest createTestInstance() {
        return randomListRootsRequest();
    }

    @Override
    protected McpListRootsRequest doParseInstance(XContentParser parser) throws IOException {
        return McpListRootsRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpListRootsRequest mutateInstance(McpListRootsRequest instance) {
        return new McpListRootsRequest(randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap)));
    }
}
