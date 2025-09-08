/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.resource;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpResourceTests extends McpSerializationTestCase<McpResource> {

    @Override
    protected Writeable.Reader<McpResource> instanceReader() {
        return McpResource::new;
    }

    @Override
    protected McpResource createTestInstance() {
        return randomResource();
    }

    @Override
    protected McpResource doParseInstance(XContentParser parser) throws IOException {
        return McpResource.PARSER.parse(parser, null);
    }

    @Override
    protected McpResource mutateInstance(McpResource instance) {
        switch (randomInt(7)) {
            case 0:
                return new McpResource(
                    randomValueOtherThan(instance.uri(), () -> randomAlphaOfLength(10)),
                    instance.name(),
                    instance.title(),
                    instance.description(),
                    instance.mimeType(),
                    instance.size(),
                    instance.annotations(),
                    instance.meta()
                );
            case 1:
                return new McpResource(
                    instance.uri(),
                    randomValueOtherThan(instance.name(), () -> randomAlphaOfLength(10)),
                    instance.title(),
                    instance.description(),
                    instance.mimeType(),
                    instance.size(),
                    instance.annotations(),
                    instance.meta()
                );
            case 2:
                return new McpResource(
                    instance.uri(),
                    instance.name(),
                    randomValueOtherThan(instance.title(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.description(),
                    instance.mimeType(),
                    instance.size(),
                    instance.annotations(),
                    instance.meta()
                );
            case 3:
                return new McpResource(
                    instance.uri(),
                    instance.name(),
                    instance.title(),
                    randomValueOtherThan(instance.description(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.mimeType(),
                    instance.size(),
                    instance.annotations(),
                    instance.meta()
                );
            case 4:
                return new McpResource(
                    instance.uri(),
                    instance.name(),
                    instance.title(),
                    instance.description(),
                    randomValueOtherThan(instance.mimeType(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.size(),
                    instance.annotations(),
                    instance.meta()
                );
            case 5:
                return new McpResource(
                    instance.uri(),
                    instance.name(),
                    instance.title(),
                    instance.description(),
                    instance.mimeType(),
                    randomValueOtherThan(instance.size(), ESTestCase::randomLongOrNull),
                    instance.annotations(),
                    instance.meta()
                );
            case 6:
                return new McpResource(
                    instance.uri(),
                    instance.name(),
                    instance.title(),
                    instance.description(),
                    instance.mimeType(),
                    instance.size(),
                    randomValueOtherThan(instance.annotations(), () -> mayBeNull(this::randomAnnotations)),
                    instance.meta()
                );
            case 7:
                return new McpResource(
                    instance.uri(),
                    instance.name(),
                    instance.title(),
                    instance.description(),
                    instance.mimeType(),
                    instance.size(),
                    instance.annotations(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
