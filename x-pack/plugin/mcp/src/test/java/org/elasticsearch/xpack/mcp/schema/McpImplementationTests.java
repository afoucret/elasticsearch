/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;

public class McpImplementationTests extends McpSerializationTestCase<McpImplementation> {

    @Override
    protected Writeable.Reader<McpImplementation> instanceReader() {
        return McpImplementation::new;
    }

    @Override
    protected McpImplementation createTestInstance() {
        return randomImplementation();
    }

    @Override
    protected McpImplementation doParseInstance(XContentParser parser) throws IOException {
        return McpImplementation.PARSER.parse(parser, null);
    }

    @Override
    protected McpImplementation mutateInstance(McpImplementation instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpImplementation(
                    randomValueOtherThan(instance.name(), () -> randomAlphaOfLength(10)),
                    instance.version(),
                    instance.title()
                );
            case 1:
                return new McpImplementation(
                    instance.name(),
                    randomValueOtherThan(instance.version(), () -> randomAlphaOfLength(10)),
                    instance.title()
                );
            case 2:
                return new McpImplementation(
                    instance.name(),
                    instance.version(),
                    randomValueOtherThan(instance.title(), () -> randomAlphaOfLengthOrNull(10))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
