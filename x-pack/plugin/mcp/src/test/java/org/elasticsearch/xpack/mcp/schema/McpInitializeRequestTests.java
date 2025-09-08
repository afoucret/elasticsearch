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

public class McpInitializeRequestTests extends McpSerializationTestCase<McpInitializeRequest> {

    @Override
    protected Writeable.Reader<McpInitializeRequest> instanceReader() {
        return McpInitializeRequest::new;
    }

    @Override
    protected McpInitializeRequest createTestInstance() {
        return randomInitializeRequest();
    }

    @Override
    protected McpInitializeRequest doParseInstance(XContentParser parser) throws IOException {
        return McpInitializeRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpInitializeRequest mutateInstance(McpInitializeRequest instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpInitializeRequest(
                    randomValueOtherThan(instance.protocolVersion(), () -> randomAlphaOfLength(10)),
                    instance.capabilities(),
                    instance.clientInfo(),
                    instance.meta()
                );
            case 1:
                return new McpInitializeRequest(
                    instance.protocolVersion(),
                    randomValueOtherThan(
                        instance.capabilities(),
                        () -> new McpClientCapabilities(
                            randomGenericMap(),
                            new McpClientCapabilities.RootCapabilities(randomOptionalBoolean()),
                            new McpClientCapabilities.Sampling(),
                            new McpClientCapabilities.Elicitation()
                        )
                    ),
                    instance.clientInfo(),
                    instance.meta()
                );
            case 2:
                return new McpInitializeRequest(
                    instance.protocolVersion(),
                    instance.capabilities(),
                    randomValueOtherThan(
                        instance.clientInfo(),
                        () -> new McpImplementation(randomAlphaOfLength(10), randomAlphaOfLength(10), randomAlphaOfLength(10))
                    ),
                    instance.meta()
                );
            case 3:
                return new McpInitializeRequest(
                    instance.protocolVersion(),
                    instance.capabilities(),
                    instance.clientInfo(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
