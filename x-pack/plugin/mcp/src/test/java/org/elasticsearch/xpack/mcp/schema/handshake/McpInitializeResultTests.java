/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.handshake;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpInitializeResultTests extends McpSerializationTestCase<McpInitializeResult> {

    @Override
    protected Writeable.Reader<McpInitializeResult> instanceReader() {
        return McpInitializeResult::new;
    }

    @Override
    protected McpInitializeResult createTestInstance() {
        return randomInitializeResult();
    }

    @Override
    protected McpInitializeResult doParseInstance(XContentParser parser) throws IOException {
        return McpInitializeResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpInitializeResult mutateInstance(McpInitializeResult instance) {
        switch (randomInt(4)) {
            case 0:
                return new McpInitializeResult(
                    randomValueOtherThan(instance.protocolVersion(), () -> randomAlphaOfLength(10)),
                    instance.capabilities(),
                    instance.serverInfo(),
                    instance.instructions(),
                    instance.meta()
                );
            case 1:
                return new McpInitializeResult(
                    instance.protocolVersion(),
                    randomValueOtherThan(instance.capabilities(), () -> mayBeNull(this::randomServerCapabilities)),
                    instance.serverInfo(),
                    instance.instructions(),
                    instance.meta()
                );
            case 2:
                return new McpInitializeResult(
                    instance.protocolVersion(),
                    instance.capabilities(),
                    randomValueOtherThan(
                        instance.serverInfo(),
                        () -> new McpImplementation(randomAlphaOfLength(10), randomAlphaOfLength(10), randomAlphaOfLength(10))
                    ),
                    instance.instructions(),
                    instance.meta()
                );
            case 3:
                return new McpInitializeResult(
                    instance.protocolVersion(),
                    instance.capabilities(),
                    instance.serverInfo(),
                    randomValueOtherThan(instance.instructions(), () -> randomAlphaOfLength(10)),
                    instance.meta()
                );
            case 4:
                return new McpInitializeResult(
                    instance.protocolVersion(),
                    instance.capabilities(),
                    instance.serverInfo(),
                    instance.instructions(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
