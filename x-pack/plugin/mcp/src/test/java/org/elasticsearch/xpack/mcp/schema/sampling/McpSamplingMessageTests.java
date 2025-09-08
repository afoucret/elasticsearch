/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.sampling;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;
import org.elasticsearch.xpack.mcp.schema.core.McpRole;

import java.io.IOException;

public class McpSamplingMessageTests extends McpSerializationTestCase<McpSamplingMessage> {

    @Override
    protected Writeable.Reader<McpSamplingMessage> instanceReader() {
        return McpSamplingMessage::new;
    }

    @Override
    protected McpSamplingMessage createTestInstance() {
        return randomSamplingMessage();
    }

    @Override
    protected McpSamplingMessage doParseInstance(XContentParser parser) throws IOException {
        return McpSamplingMessage.PARSER.parse(parser, null);
    }

    @Override
    protected McpSamplingMessage mutateInstance(McpSamplingMessage instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpSamplingMessage(
                    randomValueOtherThan(instance.role(), () -> randomFrom(McpRole.values())),
                    instance.content(),
                    instance.meta()
                );
            case 1:
                return new McpSamplingMessage(
                    instance.role(),
                    randomValueOtherThan(instance.content(), this::randomContent),
                    instance.meta()
                );
            case 2:
                return new McpSamplingMessage(
                    instance.role(),
                    instance.content(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
