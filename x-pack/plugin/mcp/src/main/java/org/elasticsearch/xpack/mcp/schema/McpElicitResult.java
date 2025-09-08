/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpElicitResult(@NotNull McpElicitResultAction action, Map<String, Object> content, Map<String, Object> meta)
    implements
        McpClientResult {

    public static final String NAME = "mcp_elicit_result";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(McpElicitResult.class, McpElicitResult.NAME, McpElicitResult::new);

    private static final ParseField ACTION_FIELD = new ParseField("action");
    private static final ParseField CONTENT_FIELD = new ParseField("content");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpElicitResult, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpElicitResult((McpElicitResultAction) args[0], (Map<String, Object>) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(constructorArg(), McpElicitResultAction::fromString, ACTION_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), CONTENT_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpElicitResult(StreamInput in) throws IOException {
        this(
            in.readEnum(McpElicitResultAction.class),
            in.readOptional(StreamInput::readGenericMap),
            in.readOptional(StreamInput::readGenericMap)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeEnum(action);
        out.writeOptional(StreamOutput::writeGenericMap, content);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(ACTION_FIELD.getPreferredName(), action.value());
        if (content != null) {
            builder.field(CONTENT_FIELD.getPreferredName(), content);
        }
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }
}
