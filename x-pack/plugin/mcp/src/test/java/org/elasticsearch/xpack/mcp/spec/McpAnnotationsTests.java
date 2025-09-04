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
import java.util.List;

public class McpAnnotationsTests extends McpSerializationTestCase<McpAnnotations> {

    @Override
    protected Writeable.Reader<McpAnnotations> instanceReader() {
        return McpAnnotations::new;
    }

    @Override
    protected McpAnnotations createTestInstance() {
        return randomAnnotations();
    }

    @Override
    protected McpAnnotations doParseInstance(XContentParser parser) throws IOException {
        return McpAnnotations.PARSER.parse(parser, null);
    }

    @Override
    protected McpAnnotations mutateInstance(McpAnnotations instance) {
        switch (randomInt(1)) {
            case 0:
                return new McpAnnotations(
                    randomValueOtherThan(instance.audience(), () -> mayBeNull(() -> List.of(randomFrom(McpRole.values())))),
                    instance.priority()
                );
            case 1:
                return new McpAnnotations(instance.audience(), randomValueOtherThan(instance.priority(), ESTestCase::randomOptionalDouble));
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
