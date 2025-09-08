/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.elicitation;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpElicitRequestTests extends McpSerializationTestCase<McpElicitRequest> {

    @Override
    protected Writeable.Reader<McpElicitRequest> instanceReader() {
        return McpElicitRequest::new;
    }

    @Override
    protected McpElicitRequest createTestInstance() {
        return randomElicitRequest();
    }

    @Override
    protected McpElicitRequest doParseInstance(XContentParser parser) throws IOException {
        return McpElicitRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpElicitRequest mutateInstance(McpElicitRequest instance) {
        switch (randomInt(2)) {
            case 0:
                return new McpElicitRequest(
                    randomValueOtherThan(instance.message(), () -> randomAlphaOfLength(10)),
                    instance.requestedSchema(),
                    instance.meta()
                );
            case 1:
                return new McpElicitRequest(
                    instance.message(),
                    randomValueOtherThan(instance.requestedSchema(), this::randomJsonSchema),
                    instance.meta()
                );
            case 2:
                return new McpElicitRequest(
                    instance.message(),
                    instance.requestedSchema(),
                    randomValueOtherThan(instance.meta(), this::randomGenericMap)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
