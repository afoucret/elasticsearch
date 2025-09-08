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

public class McpCreateMessageResultTests extends McpSerializationTestCase<McpCreateMessageResult> {

    @Override
    protected Writeable.Reader<McpCreateMessageResult> instanceReader() {
        return McpCreateMessageResult::new;
    }

    @Override
    protected McpCreateMessageResult createTestInstance() {
        return randomCreateMessageResult();
    }

    @Override
    protected McpCreateMessageResult doParseInstance(XContentParser parser) throws IOException {
        return McpCreateMessageResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpCreateMessageResult mutateInstance(McpCreateMessageResult instance) {
        switch (randomInt(4)) {
            case 0:
                return new McpCreateMessageResult(
                    randomValueOtherThan(instance.role(), () -> randomFrom(McpRole.values())),
                    instance.content(),
                    instance.model(),
                    instance.stopReason(),
                    instance.meta()
                );
            case 1:
                return new McpCreateMessageResult(
                    instance.role(),
                    randomValueOtherThan(instance.content(), this::randomContent),
                    instance.model(),
                    instance.stopReason(),
                    instance.meta()
                );
            case 2:
                return new McpCreateMessageResult(
                    instance.role(),
                    instance.content(),
                    randomValueOtherThan(instance.model(), () -> randomAlphaOfLength(10)),
                    instance.stopReason(),
                    instance.meta()
                );
            case 3:
                return new McpCreateMessageResult(
                    instance.role(),
                    instance.content(),
                    instance.model(),
                    randomValueOtherThan(instance.stopReason(), () -> randomFrom(McpStopReason.values())),
                    instance.meta()
                );
            case 4:
                return new McpCreateMessageResult(
                    instance.role(),
                    instance.content(),
                    instance.model(),
                    instance.stopReason(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
