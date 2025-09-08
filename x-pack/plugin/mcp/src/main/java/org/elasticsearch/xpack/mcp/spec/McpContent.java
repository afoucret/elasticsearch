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
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xcontent.XContentParserConfiguration;
import org.elasticsearch.xcontent.json.JsonXContent;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public sealed interface McpContent extends NamedWriteable, ToXContentObject permits McpTextContent, McpImageContent, McpResourceLink,
    McpAudioContent, McpEmbeddedResource {

    String NAME = "mcp_content";

    ParseField TYPE_FIELD = new ParseField("type");

    enum Type {
        TEXT,
        IMAGE,
        AUDIO,
        RESOURCE_LINK,
        EMBEDDED_RESOURCE;
    }

    Type type();

    void writeTo(StreamOutput out) throws IOException;

    static McpContent fromXContent(XContentParser parser) throws IOException {
        Map<String, Object> rawContent = parser.map();
        String typeValue = (String) rawContent.get("type");
        Type type;
        if (Objects.equals(McpEmbeddedResource.TYPE_VALUE, typeValue)) {
            type = Type.EMBEDDED_RESOURCE;
        } else {
            type = Type.valueOf(typeValue.toUpperCase(Locale.ROOT));
        }

        try (
            XContentBuilder builder = JsonXContent.contentBuilder().map(rawContent);
            XContentParser typeParser = XContentHelper.createParser(
                XContentParserConfiguration.EMPTY,
                BytesReference.bytes(builder),
                builder.contentType()
            )
        ) {
            return switch (type) {
                case TEXT -> McpTextContent.PARSER.apply(typeParser, null);
                case IMAGE -> McpImageContent.PARSER.apply(typeParser, null);
                case AUDIO -> McpAudioContent.PARSER.apply(typeParser, null);
                case RESOURCE_LINK -> McpResourceLink.PARSER.apply(typeParser, null);
                case EMBEDDED_RESOURCE -> McpEmbeddedResource.PARSER.apply(typeParser, null);
            };
        }
    }
}
