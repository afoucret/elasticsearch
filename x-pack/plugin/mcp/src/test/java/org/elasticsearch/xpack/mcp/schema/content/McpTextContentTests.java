/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.content;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpTextContentTests extends McpSerializationTestCase<McpTextContent> {

    @Override
    protected Writeable.Reader<McpTextContent> instanceReader() {
        return McpTextContent::new;
    }

    @Override
    protected McpTextContent createTestInstance() {
        return randomTextContent();
    }

    @Override
    protected McpTextContent mutateInstance(McpTextContent instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpTextContent(
                    randomValueOtherThan(instance.text(), () -> randomAlphaOfLength(10)),
                    instance.annotations(),
                    instance.meta()
                );
            case 1:
                return new McpTextContent(
                    instance.text(),
                    randomValueOtherThan(instance.annotations(), () -> mayBeNull(this::randomAnnotations)),
                    instance.meta()
                );
            case 2:
                return new McpTextContent(
                    instance.text(),
                    instance.annotations(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }

    @Override
    protected McpTextContent doParseInstance(XContentParser parser) throws IOException {
        return (McpTextContent) McpContent.fromXContent(parser);
    }
}
