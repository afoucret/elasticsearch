/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues.metadata;

import org.elasticsearch.cluster.Diff;
import org.elasticsearch.cluster.SimpleDiffable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;
import java.util.Objects;

public class Queue implements SimpleDiffable<Queue>, ToXContentObject {

    public static final ParseField NAME_FIELD = new ParseField("name");
    public static final ParseField SIZE_FIELD = new ParseField("size");

    @SuppressWarnings("unchecked")
    private static final ConstructingObjectParser<Queue, Void> PARSER = new ConstructingObjectParser<>("queue", args -> {
        return new Queue(
            (String) args[0],
            (int) args[1]
        );
    });

    static {
        PARSER.declareString(ConstructingObjectParser.constructorArg(), NAME_FIELD);
        PARSER.declareString(ConstructingObjectParser.constructorArg(), SIZE_FIELD);
    }

    private final String name;
    private final int size;

    public Queue(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public Queue(StreamInput in) throws IOException {
        this(in.readString(), in.readInt());
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public static Diff<Queue> readDiffFrom(StreamInput in) throws IOException {
        return SimpleDiffable.readDiffFrom(Queue::new, in);
    }

    public static Queue fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field("name", name);
        builder.field("size", size);
        builder.endObject();
        return builder;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
        out.writeInt(size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queue queue = (Queue) o;
        return size == queue.size && Objects.equals(name, queue.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size);
    }
}
