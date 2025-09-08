/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.tool;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpToolTests extends McpSerializationTestCase<McpTool> {

    @Override
    protected Writeable.Reader<McpTool> instanceReader() {
        return McpTool::new;
    }

    @Override
    protected McpTool createTestInstance() {
        return randomTool();
    }

    @Override
    protected McpTool doParseInstance(XContentParser parser) throws IOException {
        return McpTool.PARSER.parse(parser, null);
    }

    @Override
    protected McpTool mutateInstance(McpTool instance) {
        switch (randomInt(6)) {
            case 0:
                return new McpTool(
                    randomValueOtherThan(instance.name(), () -> randomAlphaOfLength(10)),
                    instance.description(),
                    instance.title(),
                    instance.inputSchema(),
                    instance.outputSchema(),
                    instance.annotations(),
                    instance.meta()
                );
            case 1:
                return new McpTool(
                    instance.name(),
                    randomValueOtherThan(instance.description(), () -> randomAlphaOfLength(10)),
                    instance.title(),
                    instance.inputSchema(),
                    instance.outputSchema(),
                    instance.annotations(),
                    instance.meta()
                );
            case 2:
                return new McpTool(
                    instance.name(),
                    instance.description(),
                    randomValueOtherThan(instance.title(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.inputSchema(),
                    instance.outputSchema(),
                    instance.annotations(),
                    instance.meta()
                );
            case 3:
                return new McpTool(
                    instance.name(),
                    instance.description(),
                    instance.title(),
                    randomValueOtherThan(instance.inputSchema(), () -> mayBeNull(this::randomJsonSchema)),
                    instance.outputSchema(),
                    instance.annotations(),
                    instance.meta()
                );
            case 4:
                return new McpTool(
                    instance.name(),
                    instance.title(),
                    instance.description(),
                    instance.inputSchema(),
                    randomValueOtherThan(instance.outputSchema(), () -> mayBeNull(this::randomGenericMap)),
                    instance.annotations(),
                    instance.meta()
                );
            case 5:
                return new McpTool(
                    instance.name(),
                    instance.description(),
                    instance.title(),
                    instance.inputSchema(),
                    instance.outputSchema(),
                    randomValueOtherThan(instance.annotations(), () -> mayBeNull(this::randomToolAnnotations)),
                    instance.meta()
                );
            case 6:
                return new McpTool(
                    instance.name(),
                    instance.description(),
                    instance.title(),
                    instance.inputSchema(),
                    instance.outputSchema(),
                    instance.annotations(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
