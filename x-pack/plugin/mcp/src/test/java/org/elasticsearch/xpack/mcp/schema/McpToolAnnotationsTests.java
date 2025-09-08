/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;

public class McpToolAnnotationsTests extends McpSerializationTestCase<McpToolAnnotations> {

    @Override
    protected Writeable.Reader<McpToolAnnotations> instanceReader() {
        return McpToolAnnotations::new;
    }

    @Override
    protected McpToolAnnotations createTestInstance() {
        return randomToolAnnotations();
    }

    @Override
    protected McpToolAnnotations doParseInstance(XContentParser parser) throws IOException {
        return McpToolAnnotations.PARSER.parse(parser, null);
    }

    @Override
    protected McpToolAnnotations mutateInstance(McpToolAnnotations instance) {
        switch (randomInt(5)) {
            case 0:
                return new McpToolAnnotations(
                    randomValueOtherThan(instance.title(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.readOnlyHint(),
                    instance.destructiveHint(),
                    instance.idempotentHint(),
                    instance.openWorldHint(),
                    instance.returnDirect()
                );
            case 1:
                return new McpToolAnnotations(
                    instance.title(),
                    randomValueOtherThan(instance.readOnlyHint(), ESTestCase::randomOptionalBoolean),
                    instance.destructiveHint(),
                    instance.idempotentHint(),
                    instance.openWorldHint(),
                    instance.returnDirect()
                );
            case 2:
                return new McpToolAnnotations(
                    instance.title(),
                    instance.readOnlyHint(),
                    randomValueOtherThan(instance.destructiveHint(), ESTestCase::randomOptionalBoolean),
                    instance.idempotentHint(),
                    instance.openWorldHint(),
                    instance.returnDirect()
                );
            case 3:
                return new McpToolAnnotations(
                    instance.title(),
                    instance.readOnlyHint(),
                    instance.destructiveHint(),
                    randomValueOtherThan(instance.idempotentHint(), ESTestCase::randomOptionalBoolean),
                    instance.openWorldHint(),
                    instance.returnDirect()
                );
            case 4:
                return new McpToolAnnotations(
                    instance.title(),
                    instance.readOnlyHint(),
                    instance.destructiveHint(),
                    instance.idempotentHint(),
                    randomValueOtherThan(instance.openWorldHint(), ESTestCase::randomOptionalBoolean),
                    instance.returnDirect()
                );
            case 5:
                return new McpToolAnnotations(
                    instance.title(),
                    instance.readOnlyHint(),
                    instance.destructiveHint(),
                    instance.idempotentHint(),
                    instance.openWorldHint(),
                    randomValueOtherThan(instance.returnDirect(), ESTestCase::randomOptionalBoolean)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
