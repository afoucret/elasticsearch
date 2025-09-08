/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xcontent.XContentParserConfiguration;
import org.elasticsearch.xcontent.json.JsonXContent;

import org.elasticsearch.xpack.mcp.schema.prompt.McpPromptReference;
import org.elasticsearch.xpack.mcp.schema.resource.McpResourceTemplateReference;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public sealed interface McpReference extends NamedWriteable, ToXContentObject permits McpPromptReference, McpResourceTemplateReference {

    String NAME = "mcp_reference";

    enum Type {
        PROMPT(McpPromptReference.TYPE_VALUE),
        RESOURCE(McpResourceTemplateReference.TYPE_VALUE);

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    Type type();

    void writeTo(StreamOutput out) throws IOException;

    static McpReference fromXContent(XContentParser parser) throws IOException {
        Map<String, Object> rawContent = parser.map();
        String typeValue = (String) rawContent.get("type");
        Type type;
        if (Objects.equals(McpPromptReference.TYPE_VALUE, typeValue)) {
            type = Type.PROMPT;
        } else if (Objects.equals(McpResourceTemplateReference.TYPE_VALUE, typeValue)) {
            type = Type.RESOURCE;
        } else {
            throw new IllegalArgumentException("Unknown reference type: " + typeValue);
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
                case PROMPT -> McpPromptReference.PARSER.apply(typeParser, null);
                case RESOURCE -> McpResourceTemplateReference.PARSER.apply(typeParser, null);
            };
        }
    }
}
