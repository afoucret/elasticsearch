/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.model;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpModelHintTests extends McpSerializationTestCase<McpModelHint> {

    @Override
    protected Writeable.Reader<McpModelHint> instanceReader() {
        return McpModelHint::new;
    }

    @Override
    protected McpModelHint createTestInstance() {
        return randomModelHint();
    }

    @Override
    protected McpModelHint doParseInstance(XContentParser parser) throws IOException {
        return McpModelHint.PARSER.parse(parser, null);
    }

    @Override
    protected McpModelHint mutateInstance(McpModelHint instance) {
        return new McpModelHint(randomValueOtherThan(instance.name(), () -> randomAlphaOfLength(10)));
    }
}
