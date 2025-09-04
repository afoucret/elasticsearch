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

public class McpElicitResultTests extends McpSerializationTestCase<McpElicitResult> {

    @Override
    protected Writeable.Reader<McpElicitResult> instanceReader() {
        return McpElicitResult::new;
    }

    @Override
    protected McpElicitResult createTestInstance() {
        return randomElicitResult();
    }

    @Override
    protected McpElicitResult doParseInstance(XContentParser parser) throws IOException {
        return McpElicitResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpElicitResult mutateInstance(McpElicitResult instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpElicitResult(
                    randomValueOtherThan(instance.action(), () -> randomFrom(McpElicitResultAction.values())),
                    instance.content(),
                    instance.meta()
                );
            case 1:
                return new McpElicitResult(
                    instance.action(),
                    randomValueOtherThan(instance.content(), this::randomGenericMap),
                    instance.meta()
                );
            case 2:
                return new McpElicitResult(
                    instance.action(),
                    instance.content(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
