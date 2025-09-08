/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import com.unboundid.util.NotNull;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public record McpCompleteResult(@NotNull Completion completion, Map<String, Object> meta) implements McpServerResult {

    public static final String NAME = "mcp_complete_result";

    public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(McpCompleteResult.class, McpCompleteResult.NAME, McpCompleteResult::new);

    private static final ParseField COMPLETION_FIELD = new ParseField("completion");
    private static final ParseField META_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    public static final ConstructingObjectParser<McpCompleteResult, Object> PARSER = new ConstructingObjectParser<>(
        NAME,
        args -> new McpCompleteResult((Completion) args[0], (Map<String, Object>) args[1])
    );

    static {
        PARSER.declareObject(constructorArg(), Completion.PARSER, COMPLETION_FIELD);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> p.map(), META_FIELD);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public McpCompleteResult(StreamInput in) throws IOException {
        this(in.readNamedWriteable(Completion.class), in.readOptional(StreamInput::readGenericMap));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeNamedWriteable(completion);
        out.writeOptional(StreamOutput::writeGenericMap, meta);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(COMPLETION_FIELD.getPreferredName(), completion);
        if (meta != null) {
            builder.field(META_FIELD.getPreferredName(), meta);
        }
        builder.endObject();
        return builder;
    }

    public record Completion(@NotNull List<String> values, Integer total, Boolean hasMore) implements NamedWriteable, ToXContentObject {

        public static final String NAME = "mcp_complete_result_completion";

        public static final Entry NAMED_WRITEABLE_ENTRY = new Entry(Completion.class, Completion.NAME, Completion::new);

        private static final ParseField VALUES_FIELD = new ParseField("values");
        private static final ParseField TOTAL_FIELD = new ParseField("total");
        private static final ParseField HAS_MORE_FIELD = new ParseField("hasMore");

        @SuppressWarnings("unchecked")
        public static final ConstructingObjectParser<Completion, Object> PARSER = new ConstructingObjectParser<>(
            NAME,
            args -> new Completion((List<String>) args[0], (Integer) args[1], (Boolean) args[2])
        );

        static {
            PARSER.declareStringArray(constructorArg(), VALUES_FIELD);
            PARSER.declareInt(optionalConstructorArg(), TOTAL_FIELD);
            PARSER.declareBoolean(optionalConstructorArg(), HAS_MORE_FIELD);
        }

        public Completion(StreamInput in) throws IOException {
            this(in.readStringCollectionAsList(), in.readOptionalVInt(), in.readOptionalBoolean());
        }

        @Override
        public String getWriteableName() {
            return NAME;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeStringCollection(values);
            out.writeOptionalVInt(total);
            out.writeOptionalBoolean(hasMore);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            builder.field(VALUES_FIELD.getPreferredName(), values);
            if (total != null) {
                builder.field(TOTAL_FIELD.getPreferredName(), total);
            }
            if (hasMore != null) {
                builder.field(HAS_MORE_FIELD.getPreferredName(), hasMore);
            }
            builder.endObject();
            return builder;
        }
    }
}
