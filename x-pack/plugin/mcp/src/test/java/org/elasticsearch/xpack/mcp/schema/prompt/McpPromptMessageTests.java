/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.prompt;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;
import org.elasticsearch.xpack.mcp.schema.core.McpRole;

import java.io.IOException;

public class McpPromptMessageTests extends McpSerializationTestCase<McpPromptMessage> {

    @Override
    protected Writeable.Reader<McpPromptMessage> instanceReader() {
        return McpPromptMessage::new;
    }

    @Override
    protected McpPromptMessage createTestInstance() {
        return randomPromptMessage();
    }

    @Override
    protected McpPromptMessage doParseInstance(XContentParser parser) throws IOException {
        return McpPromptMessage.PARSER.parse(parser, null);
    }

    @Override
    protected McpPromptMessage mutateInstance(McpPromptMessage instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpPromptMessage(randomValueOtherThan(instance.role(), () -> randomFrom(McpRole.values())), instance.content());
            case 1:
                return new McpPromptMessage(instance.role(), randomValueOtherThan(instance.content(), this::randomContent));
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
