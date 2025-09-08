/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.completion;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.mcp.schema.McpSerializationTestCase;

import java.io.IOException;

public class McpCompleteRequestTests extends McpSerializationTestCase<McpCompleteRequest> {

    @Override
    protected Writeable.Reader<McpCompleteRequest> instanceReader() {
        return McpCompleteRequest::new;
    }

    @Override
    protected McpCompleteRequest createTestInstance() {
        return randomCompleteRequest();
    }

    @Override
    protected McpCompleteRequest doParseInstance(XContentParser parser) throws IOException {
        return McpCompleteRequest.PARSER.parse(parser, null);
    }

    @Override
    protected McpCompleteRequest mutateInstance(McpCompleteRequest instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpCompleteRequest(
                    randomValueOtherThan(instance.ref(), this::randomReference),
                    instance.argument(),
                    instance.context(),
                    instance.meta()
                );
            case 1:
                return new McpCompleteRequest(
                    instance.ref(),
                    randomValueOtherThan(instance.argument(), this::randomCompleteRequestArgument),
                    instance.context(),
                    instance.meta()
                );
            case 2:
                return new McpCompleteRequest(
                    instance.ref(),
                    instance.argument(),
                    randomValueOtherThan(instance.context(), () -> mayBeNull(this::randomCompleteRequestContext)),
                    instance.meta()
                );
            case 3:
                return new McpCompleteRequest(
                    instance.ref(),
                    instance.argument(),
                    instance.context(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
