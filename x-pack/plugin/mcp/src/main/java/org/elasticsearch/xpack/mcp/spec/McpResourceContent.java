/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xcontent.XContentParserConfiguration;
import org.elasticsearch.xcontent.json.JsonXContent;

import java.io.IOException;
import java.util.Map;

public sealed interface McpResourceContent extends NamedWriteable, ToXContentObject permits McpTextResourceContents,
    McpBlobResourceContents {

    String NAME = "mcp_resource_content";

    enum Type {
        TEXT,
        BLOB
    }

    String uri();

    String mimeType();

    Map<String, Object> meta();

    void writeTo(StreamOutput out) throws IOException;

    static McpResourceContent fromXContent(XContentParser parser) throws IOException {
        Map<String, Object> rawContent = parser.map();
        Type type = rawContent.containsKey(McpBlobResourceContents.BLOB_FIELD.getPreferredName()) ? Type.BLOB : Type.TEXT;

        try (
            XContentBuilder builder = JsonXContent.contentBuilder().map(rawContent);
            XContentParser typeParser = XContentHelper.createParser(
                XContentParserConfiguration.EMPTY,
                BytesReference.bytes(builder),
                builder.contentType()
            )
        ) {
            return switch (type) {
                case TEXT -> McpTextResourceContents.PARSER.apply(typeParser, null);
                case BLOB -> McpBlobResourceContents.PARSER.apply(typeParser, null);
            };
        }
    }
}
