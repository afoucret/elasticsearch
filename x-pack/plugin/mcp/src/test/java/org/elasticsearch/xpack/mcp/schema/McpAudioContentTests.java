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

public class McpAudioContentTests extends McpSerializationTestCase<McpAudioContent> {

    @Override
    protected Writeable.Reader<McpAudioContent> instanceReader() {
        return McpAudioContent::new;
    }

    @Override
    protected McpAudioContent createTestInstance() {
        return randomAudioContent();
    }

    @Override
    protected McpAudioContent doParseInstance(XContentParser parser) throws IOException {
        return (McpAudioContent) McpContent.fromXContent(parser);
    }

    @Override
    protected McpAudioContent mutateInstance(McpAudioContent instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpAudioContent(
                    randomValueOtherThan(instance.data(), () -> randomAlphaOfLength(10)),
                    instance.mimeType(),
                    instance.annotations(),
                    instance.meta()
                );
            case 1:
                return new McpAudioContent(
                    instance.data(),
                    randomValueOtherThan(instance.mimeType(), () -> randomAlphaOfLength(10)),
                    instance.annotations(),
                    instance.meta()
                );
            case 2:
                return new McpAudioContent(
                    instance.data(),
                    instance.mimeType(),
                    randomValueOtherThan(instance.annotations(), () -> mayBeNull(this::randomAnnotations)),
                    instance.meta()
                );
            case 3:
                return new McpAudioContent(
                    instance.data(),
                    instance.mimeType(),
                    instance.annotations(),
                    randomValueOtherThan(instance.meta(), () -> mayBeNull(this::randomGenericMap))
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
