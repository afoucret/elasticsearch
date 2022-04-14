/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.cluster.metadata;

import org.elasticsearch.cluster.Diff;
import org.elasticsearch.cluster.SimpleDiffable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.core.Nullable;
import org.elasticsearch.index.Index;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class ContentIndex implements SimpleDiffable<ContentIndex>, ToXContentObject {

    public static final ParseField NAME_FIELD = new ParseField("name");
    public static final ParseField INDEX_FIELD = new ParseField("index");
    public static final ParseField METADATA_FIELD = new ParseField("_meta");

    @SuppressWarnings("unchecked")
    private static final ConstructingObjectParser<ContentIndex, Void> PARSER = new ConstructingObjectParser<>(
        "data_stream",
        args -> new ContentIndex((String) args[0], (Index) args[1], (Map<String, Object>) args[2])
    );

    static {
        PARSER.declareString(ConstructingObjectParser.constructorArg(), NAME_FIELD);
        PARSER.declareObjectArray(ConstructingObjectParser.constructorArg(), (p, c) -> Index.fromXContent(p), INDEX_FIELD);
        PARSER.declareObject(ConstructingObjectParser.optionalConstructorArg(), (p, c) -> p.map(), METADATA_FIELD);
    }

    public static Diff<ContentIndex> readDiffFrom(StreamInput in) throws IOException {
        return SimpleDiffable.readDiffFrom(ContentIndex::new, in);
    }

    private final String name;
    private final Index index;
    private final Map<String, Object> metadata;

    public ContentIndex(String name, Index index) {
        this(name, index, Collections.emptyMap());
    }

    public ContentIndex(String name, Index index,  Map<String, Object> metadata) {
        this.name = name;
        this.index = index;
        this.metadata = metadata;
    }

    public ContentIndex(StreamInput in) throws IOException {
        this(
            in.readString(),
            new Index(in),
            in.readMap()
        );
    }

    public String getName() {
        return name;
    }

    public Index getIndex() {
        return index;
    }

    @Nullable
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        index.writeTo(out);
        out.writeGenericMap(metadata);
    }

    public static ContentIndex fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(NAME_FIELD.getPreferredName(), name);
        builder.field(INDEX_FIELD.getPreferredName());
        index.toXContent(builder, params);
        if (metadata != null) {
            builder.field(METADATA_FIELD.getPreferredName(), metadata);
        }
        builder.endObject();
        return builder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentIndex that = (ContentIndex) o;
        return name.equals(that.name)
            && index.equals(that.index)
            && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, index, metadata);
    }
}
