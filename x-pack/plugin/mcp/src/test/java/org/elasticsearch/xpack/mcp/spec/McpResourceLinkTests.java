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

public class McpResourceLinkTests extends McpSerializationTestCase<McpResourceLink> {

    @Override
    protected Writeable.Reader<McpResourceLink> instanceReader() {
        return McpResourceLink::new;
    }

    @Override
    protected McpResourceLink createTestInstance() {
        return randomResourceLink();
    }

    @Override
    protected McpResourceLink doParseInstance(XContentParser parser) throws IOException {
        return (McpResourceLink) McpContent.fromXContent(parser);
    }

    @Override
    protected McpResourceLink mutateInstance(McpResourceLink instance) {
        return new McpResourceLink(randomValueOtherThan(instance.uri(), () -> randomAlphaOfLength(10)));
    }
}
