/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

/**
 * Additional properties describing a Tool to clients.
 * <p>
 * NOTE: all properties in ToolAnnotations are **hints**.
 * They are not guaranteed to provide a faithful description of
 * tool behavior (including descriptive properties like `title`).
 * <p>
 * Clients should never make tool use decisions based on ToolAnnotations
 * received from untrusted servers.
 *
 * @param title           A human-readable title for the tool.
 * @param readOnlyHint    If true, the tool does not modify its environment.
 * @param destructiveHint If true, the tool may perform destructive updates to its environment.
 * @param idempotentHint  If true, calling the tool repeatedly with the same arguments will have no additional effect.
 * @param openWorldHint   If true, this tool may interact with an "open world" of external entities.
 */
public record McpToolAnnotations(
    String title,
    Boolean readOnlyHint,
    Boolean destructiveHint,
    Boolean idempotentHint,
    Boolean openWorldHint,
    Boolean returnDirect
) implements NamedWriteable, ToXContentObject {

    public static final String NAME = "mcp_tool_annotations";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(McpToolAnnotations.class, McpToolAnnotations.NAME, McpToolAnnotations::new);

    private static final ParseField TITLE_FIELD = new ParseField("title");
    private static final ParseField READ_ONLY_HINT_FIELD = new ParseField("readOnlyHint");
    private static final ParseField DESTRUCTIVE_HINT_FIELD = new ParseField("destructiveHint");
    private static final ParseField IDEMPOTENT_HINT_FIELD = new ParseField("idempotentHint");
    private static final ParseField OPEN_WORLD_HINT_FIELD = new ParseField("openWorldHint");
    private static final ParseField RETURN_DIRECT_FIELD = new ParseField("returnDirect");

    public static final ConstructingObjectParser<McpToolAnnotations, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpToolAnnotations(
            (String) args[0],
            (Boolean) args[1],
            (Boolean) args[2],
            (Boolean) args[3],
            (Boolean) args[4],
            (Boolean) args[5]
        )
    );

    static {
        PARSER.declareString(optionalConstructorArg(), TITLE_FIELD);
        PARSER.declareBoolean(optionalConstructorArg(), READ_ONLY_HINT_FIELD);
        PARSER.declareBoolean(optionalConstructorArg(), DESTRUCTIVE_HINT_FIELD);
        PARSER.declareBoolean(optionalConstructorArg(), IDEMPOTENT_HINT_FIELD);
        PARSER.declareBoolean(optionalConstructorArg(), OPEN_WORLD_HINT_FIELD);
        PARSER.declareBoolean(optionalConstructorArg(), RETURN_DIRECT_FIELD);
    }

    public McpToolAnnotations(StreamInput in) throws IOException {
        this(
            in.readOptionalString(),
            in.readOptionalBoolean(),
            in.readOptionalBoolean(),
            in.readOptionalBoolean(),
            in.readOptionalBoolean(),
            in.readOptionalBoolean()
        );
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalString(title);
        out.writeOptionalBoolean(readOnlyHint);
        out.writeOptionalBoolean(destructiveHint);
        out.writeOptionalBoolean(idempotentHint);
        out.writeOptionalBoolean(openWorldHint);
        out.writeOptionalBoolean(returnDirect);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (title != null) {
            builder.field(TITLE_FIELD.getPreferredName(), title);
        }

        if (readOnlyHint != null) {
            builder.field(READ_ONLY_HINT_FIELD.getPreferredName(), readOnlyHint);
        }

        if (destructiveHint != null) {
            builder.field(DESTRUCTIVE_HINT_FIELD.getPreferredName(), destructiveHint);
        }

        if (idempotentHint != null) {
            builder.field(IDEMPOTENT_HINT_FIELD.getPreferredName(), idempotentHint);
        }

        if (openWorldHint != null) {
            builder.field(OPEN_WORLD_HINT_FIELD.getPreferredName(), openWorldHint);
        }

        if (returnDirect != null) {
            builder.field(RETURN_DIRECT_FIELD.getPreferredName(), returnDirect);
        }

        builder.endObject();
        return builder;
    }
}
