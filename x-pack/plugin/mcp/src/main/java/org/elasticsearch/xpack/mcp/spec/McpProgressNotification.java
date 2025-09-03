/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpProgressNotification(String progressToken, McpContent content, Map<String, Object> meta) implements McpNotification {

    private static final ParseField PROGRESS_TOKEN = new ParseField("progressToken");
    private static final ParseField CONTENT = new ParseField("content");
    private static final ParseField META = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpProgressNotification, Void> PARSER = new ConstructingObjectParser<>(
        "mcp_progress_notification",
        args -> new McpProgressNotification((String) args[0], (McpContent) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), PROGRESS_TOKEN);
        PARSER.declareNamedObject(constructorArg(), (p, c, n) -> p.namedObject(McpContent.class, n, c), CONTENT);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META);
    }

    public McpProgressNotification(StreamInput in) throws IOException {
        this(in.readString(), in.readNamedWriteable(McpContent.class), in.readMap(StreamInput::readString, StreamInput::readGenericValue));
    }

    @Override
    public String getWriteableName() {
        return "mcp_progress_notification";
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(progressToken);
        out.writeNamedWriteable(content);
        out.writeMap(meta, StreamOutput::writeString, StreamOutput::writeGenericValue);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (progressToken != null) {
            builder.field(PROGRESS_TOKEN.getPreferredName(), progressToken);
        }
        if (content != null) {
            builder.field(CONTENT.getPreferredName(), content);
        }
        if (meta != null) {
            builder.field(META.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
