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

public class McpResourceTemplateTests extends McpSerializationTestCase<McpResourceTemplate> {

    @Override
    protected Writeable.Reader<McpResourceTemplate> instanceReader() {
        return McpResourceTemplate::new;
    }

    @Override
    protected McpResourceTemplate createTestInstance() {
        return randomResourceTemplate();
    }

    @Override
    protected McpResourceTemplate doParseInstance(XContentParser parser) throws IOException {
        return McpResourceTemplate.PARSER.parse(parser, null);
    }

    @Override
    protected McpResourceTemplate mutateInstance(McpResourceTemplate instance) {
        switch (randomInt(6)) {
            case 0:
                return new McpResourceTemplate(
                    randomValueOtherThan(instance.name(), () -> randomAlphaOfLength(10)),
                    instance.title(),
                    instance.uriTemplate(),
                    instance.description(),
                    instance.mimeType(),
                    instance.annotations(),
                    instance.meta()
                );
            case 1:
                return new McpResourceTemplate(
                    instance.name(),
                    randomValueOtherThan(instance.title(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.uriTemplate(),
                    instance.description(),
                    instance.mimeType(),
                    instance.annotations(),
                    instance.meta()
                );
            case 2:
                return new McpResourceTemplate(
                    instance.name(),
                    instance.title(),
                    randomValueOtherThan(instance.uriTemplate(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.description(),
                    instance.mimeType(),
                    instance.annotations(),
                    instance.meta()
                );
            case 3:
                return new McpResourceTemplate(
                    instance.name(),
                    instance.title(),
                    instance.uriTemplate(),
                    randomValueOtherThan(instance.description(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.mimeType(),
                    instance.annotations(),
                    instance.meta()
                );
            case 4:
                return new McpResourceTemplate(
                    instance.name(),
                    instance.title(),
                    instance.uriTemplate(),
                    instance.description(),
                    randomValueOtherThan(instance.mimeType(), () -> randomAlphaOfLengthOrNull(10)),
                    instance.annotations(),
                    instance.meta()
                );
            case 5:
                return new McpResourceTemplate(
                    instance.name(),
                    instance.title(),
                    instance.uriTemplate(),
                    instance.description(),
                    instance.mimeType(),
                    randomValueOtherThan(instance.annotations(), () -> mayBeNull(this::randomAnnotations)),
                    instance.meta()
                );
            case 6:
                return new McpResourceTemplate(
                    instance.name(),
                    instance.title(),
                    instance.uriTemplate(),
                    instance.description(),
                    instance.mimeType(),
                    instance.annotations(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
