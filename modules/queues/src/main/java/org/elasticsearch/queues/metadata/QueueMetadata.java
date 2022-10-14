/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues.metadata;

import org.elasticsearch.Version;
import org.elasticsearch.cluster.Diff;
import org.elasticsearch.cluster.DiffableUtils;
import org.elasticsearch.cluster.NamedDiff;
import org.elasticsearch.cluster.metadata.Metadata;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.util.Maps;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;

public class QueueMetadata implements Metadata.Custom {

    public static final QueueMetadata EMPTY = new QueueMetadata(Collections.emptyMap());

    public static final String TYPE = "queues";
    private static final ParseField QUEUES_FIELD = new ParseField("queues");

    @SuppressWarnings("unchecked")
    private static final ConstructingObjectParser<QueueMetadata, Void> PARSER = new ConstructingObjectParser<>(TYPE, false, args -> {
        return new QueueMetadata((Map<String, Queue>) args[0]);
    });

    static {
        PARSER.declareObject(ConstructingObjectParser.constructorArg(), (p, c) -> {
            ImmutableOpenMap.Builder<String, Queue> queues = ImmutableOpenMap.builder();
            while (p.nextToken() != XContentParser.Token.END_OBJECT) {
                String name = p.currentName();
                queues.put(name, Queue.fromXContent(p));
            }
            return queues.build();
        }, QUEUES_FIELD);
    }

    private final Map<String, Queue> queues;

    public QueueMetadata(Map<String, Queue> queues) {
        this.queues = queues;
    }

    public QueueMetadata(StreamInput in) throws IOException {
        int size = in.readVInt();
        Map<String, Queue> queues = Maps.newMapWithExpectedSize(size);
        for (int i = 0; i < size; i++) {
            Queue queue = new Queue(in);
            queues.put(queue.getName(), queue);
        }

        this.queues = Collections.unmodifiableMap(queues);
    }

    public Map<String, Queue> getQueues() {
        return queues;
    }

    public static QueueMetadata fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.xContentValuesMap(QUEUES_FIELD.getPreferredName(), queues);

        return builder;
    }

    @Override
    public Diff<Metadata.Custom> diff(Metadata.Custom previousState) {
        return new QueueMetadataDiff((QueueMetadata) previousState, this);
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
        return Version.V_8_6_0;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeVInt(queues.size());
        for (Queue queue : queues.values()) {
            queue.writeTo(out);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueueMetadata that = (QueueMetadata) o;
        return Objects.equals(queues, that.queues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queues);
    }

    static class QueueMetadataDiff implements NamedDiff<Metadata.Custom> {

        final Diff<Map<String, Queue>> queues;

        QueueMetadataDiff(QueueMetadata before, QueueMetadata after) {
            this.queues = DiffableUtils.diff(before.queues, after.queues, DiffableUtils.getStringKeySerializer());
        }

        QueueMetadataDiff(StreamInput in) throws IOException {
            queues = DiffableUtils.readJdkMapDiff(
                in,
                DiffableUtils.getStringKeySerializer(),
                Queue::new,
                Queue::readDiffFrom
            );
        }

        @Override
        public Metadata.Custom apply(Metadata.Custom part) {
            return new QueueMetadata(queues.apply(((QueueMetadata) part).queues));
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            queues.writeTo(out);
        }

        @Override
        public String getWriteableName() {
            return TYPE;
        }

        @Override
        public Version getMinimalSupportedVersion() {
            return Version.V_8_6_0;
        }
    }
}
