/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.completion;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpCompleteResultTests extends McpSerializationTestCase<McpCompleteResult> {

    @Override
    protected Writeable.Reader<McpCompleteResult> instanceReader() {
        return McpCompleteResult::new;
    }

    @Override
    protected McpCompleteResult createTestInstance() {
        return randomCompleteResult();
    }

    @Override
    protected McpCompleteResult doParseInstance(XContentParser parser) throws IOException {
        return McpCompleteResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpCompleteResult mutateInstance(McpCompleteResult instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpCompleteResult(randomValueOtherThan(instance.completion(), this::randomCompletion), instance.meta());
            case 1:
                return new McpCompleteResult(
                    instance.completion(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
