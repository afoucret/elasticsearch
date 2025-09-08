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

import java.io.IOException;

public class McpGetPromptRequestTests extends McpSerializationTestCase<McpGetPromptRequest> {

    @Override
    protected Writeable.Reader<McpGetPromptRequest> instanceReader() {
        return McpGetPromptRequest::new;
    }

    @Override
    protected McpGetPromptRequest createTestInstance() {
        return randomGetPromptRequest();
    }

    @Override
    protected McpGetPromptRequest doParseInstance(XContentParser parser) throws IOException {
        return McpGetPromptRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpGetPromptRequest mutateInstance(McpGetPromptRequest instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpGetPromptRequest(
                    randomValueOtherThan(instance.name(), () -> randomAlphaOfLength(10)),
                    instance.arguments(),
                    instance.meta()
                );
            case 1:
                return new McpGetPromptRequest(
                    instance.name(),
                    randomValueOtherThan(instance.arguments(), () -> mayBeNull(this::randomGenericMap)),
                    instance.meta()
                );
            case 2:
                return new McpGetPromptRequest(
                    instance.name(),
                    instance.arguments(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
