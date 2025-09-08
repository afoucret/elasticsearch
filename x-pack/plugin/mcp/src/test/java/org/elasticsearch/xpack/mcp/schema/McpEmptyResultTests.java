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

public class McpEmptyResultTests extends McpSerializationTestCase<McpEmptyResult> {

    @Override
    protected Writeable.Reader<McpEmptyResult> instanceReader() {
        return McpEmptyResult::new;
    }

    @Override
    protected McpEmptyResult createTestInstance() {
        return randomEmptyResult();
    }

    @Override
    protected McpEmptyResult doParseInstance(XContentParser parser) throws IOException {
        return McpEmptyResult.PARSER.parse(parser, null);
    }

    @Override
    protected McpEmptyResult mutateInstance(McpEmptyResult instance) {
        return new McpEmptyResult(randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap)));
    }
}
