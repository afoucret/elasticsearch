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

public class McpImageContentTests extends McpSerializationTestCase<McpImageContent> {

    @Override
    protected Writeable.Reader<McpImageContent> instanceReader() {
        return McpImageContent::new;
    }

    @Override
    protected McpImageContent createTestInstance() {
        return randomImageContent();
    }

    @Override
    protected McpImageContent doParseInstance(XContentParser parser) throws IOException {
        return (McpImageContent) McpContent.fromXContent(parser);
    }

    @Override
    protected McpImageContent mutateInstance(McpImageContent instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpImageContent(
                    randomValueOtherThan(instance.data(), () -> randomAlphaOfLength(10)),
                    instance.mimeType(),
                    instance.annotations(),
                    instance.meta()
                );
            case 1:
                return new McpImageContent(
                    instance.data(),
                    randomValueOtherThan(instance.mimeType(), () -> randomAlphaOfLength(10)),
                    instance.annotations(),
                    instance.meta()
                );
            case 2:
                return new McpImageContent(
                    instance.data(),
                    instance.mimeType(),
                    randomValueOtherThan(instance.annotations(), () -> mayBeNull(this::randomAnnotations)),
                    instance.meta()
                );
            case 3:
                return new McpImageContent(
                    instance.data(),
                    instance.mimeType(),
                    instance.annotations(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
