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

public class McpEmbeddedResourceTests extends McpSerializationTestCase<McpEmbeddedResource> {

    @Override
    protected Writeable.Reader<McpEmbeddedResource> instanceReader() {
        return McpEmbeddedResource::new;
    }

    @Override
    protected McpEmbeddedResource createTestInstance() {
        return randomEmbeddedResource();
    }

    @Override
    protected McpEmbeddedResource doParseInstance(XContentParser parser) throws IOException {
        return McpEmbeddedResource.PARSER.parse(parser, null);
    }

    @Override
    protected McpEmbeddedResource mutateInstance(McpEmbeddedResource instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpEmbeddedResource(
                    randomValueOtherThan(instance.resource(), this::randomResourceContent),
                    instance.annotations(),
                    instance.meta()
                );
            case 1:
                return new McpEmbeddedResource(
                    instance.resource(),
                    randomValueOtherThan(instance.annotations(), this::randomAnnotations),
                    instance.meta()
                );
            case 2:
                return new McpEmbeddedResource(
                    instance.resource(),
                    instance.annotations(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
