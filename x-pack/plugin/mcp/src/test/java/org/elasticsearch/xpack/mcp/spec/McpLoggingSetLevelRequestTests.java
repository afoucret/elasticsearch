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

public class McpLoggingSetLevelRequestTests extends McpSerializationTestCase<McpLoggingSetLevelRequest> {

    @Override
    protected Writeable.Reader<McpLoggingSetLevelRequest> instanceReader() {
        return McpLoggingSetLevelRequest::new;
    }

    @Override
    protected McpLoggingSetLevelRequest createTestInstance() {
        return randomLoggingSetLevelRequest();
    }

    @Override
    protected McpLoggingSetLevelRequest doParseInstance(XContentParser parser) throws IOException {
        return McpLoggingSetLevelRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpLoggingSetLevelRequest mutateInstance(McpLoggingSetLevelRequest instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpLoggingSetLevelRequest(
                    randomValueOtherThan(instance.level(), () -> randomAlphaOfLength(10)),
                    instance.meta()
                );
            case 1:
                return new McpLoggingSetLevelRequest(
                    instance.level(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
