/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.event;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xpack.application.analytics.AnalyticsTemplateRegistry;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * This class represents Analytics events object meant to be emitted to the event queue.
 * Subclasses are implementing the different event types.
 */
public abstract class AnalyticsEvent implements Writeable, ToXContentObject {

    public static final ParseField TIMESTAMP_FIELD = new ParseField("@timestamp");
    public static final ParseField EVENT_FIELD = new ParseField("event");
    public static final ParseField EVENT_ACTION_FIELD = new ParseField("action");
    public static final ParseField DATA_STREAM_FIELD = new ParseField("data_stream");
    public static final ParseField DATA_STREAM_TYPE_FIELD = new ParseField("type");
    public static final ParseField DATA_STREAM_NAMESPACE_FIELD = new ParseField("namespace");
    public static final ParseField DATA_STREAM_DATASET_FIELD = new ParseField("dataset");

    /**
     * Analytics context. Used to carry information to parsers.
     */
    public interface Context {
        long eventTime();

        Type eventType();

        String eventCollectionName();
    }

    /**
     * Analytics event types.
     */
    public enum Type {
        PAGEVIEW("pageview"),
        SEARCH("search"),
        SEARCH_CLICK("search_click");

        private final String typeName;

        Type(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public String toString() {
            return typeName.toLowerCase(Locale.ROOT);
        }
    }

    private final String eventCollectionName;

    private final long eventTime;

    private final AnalyticsEventSessionData session;

    private final AnalyticsEventUserData user;

    protected AnalyticsEvent(String eventCollectionName, long eventTime, AnalyticsEventSessionData session, AnalyticsEventUserData user) {
        this.eventCollectionName = Strings.requireNonBlank(eventCollectionName, "eventCollectionName");
        this.eventTime = eventTime;
        this.session = Objects.requireNonNull(session, AnalyticsEventSessionData.SESSION_FIELD.getPreferredName());
        this.user = Objects.requireNonNull(user, AnalyticsEventUserData.USER_FIELD.getPreferredName());
    }

    protected AnalyticsEvent(StreamInput in) throws IOException {
        this(in.readString(), in.readLong(), new AnalyticsEventSessionData(in), new AnalyticsEventUserData(in));
    }

    public abstract Type eventType();

    public String eventCollectionName() {
        return eventCollectionName;
    }

    public long eventTime() {
        return eventTime;
    }

    public AnalyticsEventSessionData session() {
        return session;
    }

    public AnalyticsEventUserData user() {
        return user;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(eventCollectionName);
        out.writeLong(eventTime);
        session.writeTo(out);
        user.writeTo(out);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        {
            builder.field(TIMESTAMP_FIELD.getPreferredName(), eventTime());

            builder.startObject(EVENT_FIELD.getPreferredName());
            {
                builder.field(EVENT_ACTION_FIELD.getPreferredName(), eventType());
            }
            builder.endObject();

            builder.startObject(DATA_STREAM_FIELD.getPreferredName());
            {
                builder.field(DATA_STREAM_TYPE_FIELD.getPreferredName(), AnalyticsTemplateRegistry.EVENT_DATA_STREAM_TYPE);
                builder.field(DATA_STREAM_DATASET_FIELD.getPreferredName(), AnalyticsTemplateRegistry.EVENT_DATA_STREAM_DATASET);
                builder.field(DATA_STREAM_NAMESPACE_FIELD.getPreferredName(), eventCollectionName());

            }
            builder.endObject();

            builder.field(AnalyticsEventSessionData.SESSION_FIELD.getPreferredName(), session());
            builder.field(AnalyticsEventUserData.USER_FIELD.getPreferredName(), user());

            // Render additional fields from the event payload (session, user, page, ...)
            addCustomFieldToXContent(builder, params);

        }
        builder.endObject();

        return builder;
    }

    @Override
    public boolean isFragment() {
        return false;
    }

    protected abstract void addCustomFieldToXContent(XContentBuilder builder, Params params) throws IOException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalyticsEvent that = (AnalyticsEvent) o;
        return eventCollectionName.equals(that.eventCollectionName)
            && eventTime == that.eventTime
            && Objects.equals(session, that.session)
            && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventCollectionName, eventTime, session, user);
    }
}
