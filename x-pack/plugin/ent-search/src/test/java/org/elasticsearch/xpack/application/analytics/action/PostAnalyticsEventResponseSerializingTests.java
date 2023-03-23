/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.action;

import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.AbstractWireSerializingTestCase;
import org.elasticsearch.xcontent.XContentType;
import org.elasticsearch.xpack.application.analytics.AnalyticsCollection;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsContext;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEvent;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEventType;

import java.io.IOException;

public class PostAnalyticsEventResponseSerializingTests extends AbstractWireSerializingTestCase<PostAnalyticsEventAction.Response> {

    @Override
    protected Writeable.Reader<PostAnalyticsEventAction.Response> instanceReader() {
        return PostAnalyticsEventAction.Response::new;
    }

    @Override
    protected PostAnalyticsEventAction.Response createTestInstance() {
        boolean debug = randomBoolean();

        if (debug) return new PostAnalyticsEventAction.Response(randomBoolean(), randomAnalyticsEvent());

        return new PostAnalyticsEventAction.Response(randomBoolean());
    }

    @Override
    protected PostAnalyticsEventAction.Response mutateInstance(PostAnalyticsEventAction.Response instance) throws IOException {
        return randomValueOtherThan(instance, this::createTestInstance);
    }

    private AnalyticsEvent randomAnalyticsEvent() {
        AnalyticsContext analyticsContext = new AnalyticsContext(
            new AnalyticsCollection(randomIdentifier()),
            randomFrom(AnalyticsEventType.values()),
            randomLong()
        );
        return new AnalyticsEvent(analyticsContext, randomFrom(XContentType.values()), new BytesArray(randomIdentifier()));
    }
}
