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

public class McpBlobResourceContentsTests extends McpSerializationTestCase<McpBlobResourceContents> {

    @Override
    protected Writeable.Reader<McpBlobResourceContents> instanceReader() {
        return in -> (McpBlobResourceContents) McpResourceContent.read(in);
    }

    @Override
    protected McpBlobResourceContents createTestInstance() {
        return randomBlobResourceContents();
    }

    @Override
    protected McpBlobResourceContents doParseInstance(XContentParser parser) throws IOException {
        return McpBlobResourceContents.PARSER.parse(parser, null);
    }

    @Override
    protected McpBlobResourceContents mutateInstance(McpBlobResourceContents instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpBlobResourceContents(
                    randomValueOtherThan(instance.uri(), () -> randomAlphaOfLength(10)),
                    instance.mimeType(),
                    instance.blob(),
                    instance.meta()
                );
            case 1:
                return new McpBlobResourceContents(
                    instance.uri(),
                    randomValueOtherThan(instance.mimeType(), () -> randomAlphaOfLength(10)),
                    instance.blob(),
                    instance.meta()
                );
            case 2:
                return new McpBlobResourceContents(
                    instance.uri(),
                    instance.mimeType(),
                    randomValueOtherThan(instance.blob(), () -> randomAlphaOfLength(10)),
                    instance.meta()
                );
            case 3:
                return new McpBlobResourceContents(
                    instance.uri(),
                    instance.mimeType(),
                    instance.blob(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
