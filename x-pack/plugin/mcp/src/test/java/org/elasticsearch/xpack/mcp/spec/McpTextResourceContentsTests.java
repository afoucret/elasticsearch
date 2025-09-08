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

public class McpTextResourceContentsTests extends McpSerializationTestCase<McpTextResourceContents> {

    @Override
    protected Writeable.Reader<McpTextResourceContents> instanceReader() {
        return McpTextResourceContents::new;
    }

    @Override
    protected McpTextResourceContents createTestInstance() {
        return new McpTextResourceContents(randomAlphaOfLength(10), randomAlphaOfLength(10), randomAlphaOfLength(10), randomGenericMap());
    }

    @Override
    protected McpTextResourceContents doParseInstance(XContentParser parser) throws IOException {
        return (McpTextResourceContents) McpResourceContent.fromXContent(parser);
    }

    @Override
    protected McpTextResourceContents mutateInstance(McpTextResourceContents instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpTextResourceContents(
                    randomValueOtherThan(instance.uri(), () -> randomAlphaOfLength(10)),
                    instance.mimeType(),
                    instance.text(),
                    instance.meta()
                );
            case 1:
                return new McpTextResourceContents(
                    instance.uri(),
                    randomValueOtherThan(instance.mimeType(), () -> randomAlphaOfLength(10)),
                    instance.text(),
                    instance.meta()
                );
            case 2:
                return new McpTextResourceContents(
                    instance.uri(),
                    instance.mimeType(),
                    randomValueOtherThan(instance.text(), () -> randomAlphaOfLength(10)),
                    instance.meta()
                );
            case 3:
                return new McpTextResourceContents(
                    instance.uri(),
                    instance.mimeType(),
                    instance.text(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
