/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.cluster.metadata;

import org.elasticsearch.Version;
import org.elasticsearch.cluster.Diff;
import org.elasticsearch.cluster.DiffableUtils;
import org.elasticsearch.cluster.NamedDiff;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContentIndexMetadata implements Metadata.Custom {

    public static final String TYPE = "content_indices";
    public static final ParseField CONTENT_INDICES = new ParseField("content_indices");

    private final Map<String, ContentIndex> contentIndices;

    @SuppressWarnings("unchecked")
    private static final ConstructingObjectParser<ContentIndexMetadata, Void> PARSER = new ConstructingObjectParser<>(TYPE, false, args -> {
        Map<String, ContentIndex> contentIndices = (Map<String, ContentIndex>) args[0];
        return new ContentIndexMetadata(contentIndices);
    });

    static {
        PARSER.declareObject(ConstructingObjectParser.constructorArg(), (p, c) -> {
            Map<String, ContentIndex> contentIndices = new HashMap<>();
            while (p.nextToken() != XContentParser.Token.END_OBJECT) {
                String name = p.currentName();
                contentIndices.put(name, ContentIndex.fromXContent(p));
            }
            return contentIndices;
        }, CONTENT_INDICES);
    }

    public ContentIndexMetadata(Map<String, ContentIndex> contentIndices) {
        this.contentIndices = contentIndices;
    }

    public ContentIndexMetadata(StreamInput in) throws IOException {
        this(in.readMap(StreamInput::readString, ContentIndex::new));
    }

    public Map<String, ContentIndex> contentIndices() {
        return contentIndices;
    }

    @Override
    public Diff<Metadata.Custom> diff(Metadata.Custom before) {
        return new ContentIndexMetadata.ContentIndexMetadataDiff((ContentIndexMetadata) before, this);
    }

    public static NamedDiff<Metadata.Custom> readDiffFrom(StreamInput in) throws IOException {
        return new ContentIndexMetadata.ContentIndexMetadataDiff(in);
    }

    @Override
    public EnumSet<Metadata.XContentContext> context() {
        return Metadata.ALL_CONTEXTS;
    }

    @Override
    public String getWriteableName() {
        return TYPE;
    }

    @Override
    public Version getMinimalSupportedVersion() {
        return Version.V_8_3_0;
    }

    public static ContentIndexMetadata fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.xContentValuesMap(CONTENT_INDICES.getPreferredName(), contentIndices);
        return builder;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeMap(this.contentIndices, StreamOutput::writeString, (stream, val) -> val.writeTo(stream));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.contentIndices);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ContentIndexMetadata other = (ContentIndexMetadata) obj;

        return Objects.equals(this.contentIndices, other.contentIndices);
    }

    @Override
    public String toString() {
        return Strings.toString(this);
    }

    static class ContentIndexMetadataDiff implements NamedDiff<Metadata.Custom> {

        final Diff<Map<String, ContentIndex>> contentIndicesDiff;

        ContentIndexMetadataDiff(ContentIndexMetadata before, ContentIndexMetadata after) {
            this.contentIndicesDiff = DiffableUtils.diff(
                before.contentIndices,
                after.contentIndices,
                DiffableUtils.getStringKeySerializer()
            );
        }

        ContentIndexMetadataDiff(StreamInput in) throws IOException {
            this.contentIndicesDiff = DiffableUtils.readJdkMapDiff(
                in,
                DiffableUtils.getStringKeySerializer(),
                ContentIndex::new,
                ContentIndex::readDiffFrom
            );
        }

        @Override
        public Metadata.Custom apply(Metadata.Custom part) {
            return new ContentIndexMetadata(
                contentIndicesDiff.apply(((ContentIndexMetadata) part).contentIndices)
            );
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            contentIndicesDiff.writeTo(out);
        }

        @Override
        public String getWriteableName() {
            return TYPE;
        }

        @Override
        public Version getMinimalSupportedVersion() {
            return Version.V_8_3_0;
        }
    }
}
