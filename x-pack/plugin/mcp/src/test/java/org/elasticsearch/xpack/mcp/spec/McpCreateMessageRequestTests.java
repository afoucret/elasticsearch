/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;

public class McpCreateMessageRequestTests extends McpSerializationTestCase<McpCreateMessageRequest> {

    @Override
    protected Writeable.Reader<McpCreateMessageRequest> instanceReader() {
        return McpCreateMessageRequest::new;
    }

    @Override
    protected McpCreateMessageRequest createTestInstance() {
        return randomCreateMessageRequest();
    }

    @Override
    protected McpCreateMessageRequest doParseInstance(XContentParser parser) throws IOException {
        return McpCreateMessageRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpCreateMessageRequest mutateInstance(McpCreateMessageRequest instance) {
        switch (randomInt(8)) {
            case 0:
                return new McpCreateMessageRequest(
                    randomValueOtherThan(instance.messages(), () -> randomList(10, this::randomSamplingMessage)),
                    instance.maxTokens(),
                    instance.modelPreferences(),
                    instance.systemPrompt(),
                    instance.includeContext(),
                    instance.temperature(),
                    instance.stopSequences(),
                    instance.metadata(),
                    instance.meta()
                );
            case 1:
                return new McpCreateMessageRequest(
                    instance.messages(),
                    randomValueOtherThan(instance.maxTokens(), ESTestCase::randomInt),
                    instance.modelPreferences(),
                    instance.systemPrompt(),
                    instance.includeContext(),
                    instance.temperature(),
                    instance.stopSequences(),
                    instance.metadata(),
                    instance.meta()
                );
            case 2:
                return new McpCreateMessageRequest(
                    instance.messages(),
                    instance.maxTokens(),
                    randomValueOtherThan(instance.modelPreferences(), this::randomModelPreferences),
                    instance.systemPrompt(),
                    instance.includeContext(),
                    instance.temperature(),
                    instance.stopSequences(),
                    instance.metadata(),
                    instance.meta()
                );
            case 3:
                return new McpCreateMessageRequest(
                    instance.messages(),
                    instance.maxTokens(),
                    instance.modelPreferences(),
                    randomValueOtherThan(instance.systemPrompt(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.includeContext(),
                    instance.temperature(),
                    instance.stopSequences(),
                    instance.metadata(),
                    instance.meta()
                );
            case 4:
                return new McpCreateMessageRequest(
                    instance.messages(),
                    instance.maxTokens(),
                    instance.modelPreferences(),
                    instance.systemPrompt(),
                    randomValueOtherThan(instance.includeContext(), () -> randomFrom(McpIncludeContext.values())),
                    instance.temperature(),
                    instance.stopSequences(),
                    instance.metadata(),
                    instance.meta()
                );
            case 5:
                return new McpCreateMessageRequest(
                    instance.messages(),
                    instance.maxTokens(),
                    instance.modelPreferences(),
                    instance.systemPrompt(),
                    instance.includeContext(),
                    randomValueOtherThan(instance.temperature(), ESTestCase::randomDouble),
                    instance.stopSequences(),
                    instance.metadata(),
                    instance.meta()
                );
            case 6:
                return new McpCreateMessageRequest(
                    instance.messages(),
                    instance.maxTokens(),
                    instance.modelPreferences(),
                    instance.systemPrompt(),
                    instance.includeContext(),
                    instance.temperature(),
                    randomValueOtherThan(instance.stopSequences(), () -> randomList(10, () -> randomAlphaOfLength(10))),
                    instance.metadata(),
                    instance.meta()
                );
            case 7:
                return new McpCreateMessageRequest(
                    instance.messages(),
                    instance.maxTokens(),
                    instance.modelPreferences(),
                    instance.systemPrompt(),
                    instance.includeContext(),
                    instance.temperature(),
                    instance.stopSequences(),
                    randomValueOtherThan(instance.metadata(), this::randomGenericMap),
                    instance.meta()
                );
            case 8:
                return new McpCreateMessageRequest(
                    instance.messages(),
                    instance.maxTokens(),
                    instance.modelPreferences(),
                    instance.systemPrompt(),
                    instance.includeContext(),
                    instance.temperature(),
                    instance.stopSequences(),
                    instance.metadata(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
