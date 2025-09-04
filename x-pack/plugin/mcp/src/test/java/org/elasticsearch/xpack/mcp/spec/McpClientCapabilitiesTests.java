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

public class McpClientCapabilitiesTests extends McpSerializationTestCase<McpClientCapabilities> {

    @Override
    protected Writeable.Reader<McpClientCapabilities> instanceReader() {
        return McpClientCapabilities::new;
    }

    @Override
    protected McpClientCapabilities createTestInstance() {
        return randomClientCapabilities();
    }

    @Override
    protected McpClientCapabilities doParseInstance(XContentParser parser) throws IOException {
        return McpClientCapabilities.PARSER.parse(parser, null);
    }

    @Override
    protected McpClientCapabilities mutateInstance(McpClientCapabilities instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpClientCapabilities(
                    randomValueOtherThan(instance.experimental(), this::randomGenericMap),
                    instance.roots(),
                    instance.sampling(),
                    instance.elicitation()
                );
            case 1:
                return new McpClientCapabilities(
                    instance.experimental(),
                    randomValueOtherThan(
                        instance.roots(),
                        () -> mayBeNull(() -> new McpClientCapabilities.RootCapabilities(randomOptionalBoolean()))
                    ),
                    instance.sampling(),
                    instance.elicitation()
                );
            case 2:
                return new McpClientCapabilities(
                    instance.experimental(),
                    instance.roots(),
                    instance.sampling() == null ? new McpClientCapabilities.Sampling() : null,
                    instance.elicitation()
                );
            case 3:
                return new McpClientCapabilities(
                    instance.experimental(),
                    instance.roots(),
                    instance.sampling(),
                    instance.elicitation() == null ? new McpClientCapabilities.Elicitation() : null
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
