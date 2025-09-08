/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.tool;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xpack.mcp.schema.core.McpClientRequest;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCallToolRequest(@NotNull String name, Map<String, Object> arguments, Map<String, Object> meta)
    implements
        McpClientRequest {

    public static final String NAME = "mcp_call_tool_request";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(McpCallToolRequest.class, McpCallToolRequest.NAME, McpCallToolRequest::new);

    private static final ParseField NAME_FIELD = new ParseField("name");
    private static final ParseField ARGUMENTS_FIELD = new ParseField("arguments");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCallToolRequest, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpCallToolRequest((String) args[0], (Map<String, Object>) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), NAME_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), ARGUMENTS_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpCallToolRequest(StreamInput in) throws IOException {
        this(in.readString(), in.readOptional(StreamInput::readGenericMap), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        out.writeOptional(StreamOutput::writeGenericMap, arguments);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
        builder.startObject();
        builder.field(NAME_FIELD.getPreferredName(), name);
        if (arguments != null) {
            builder.field(ARGUMENTS_FIELD.getPreferredName(), arguments);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
