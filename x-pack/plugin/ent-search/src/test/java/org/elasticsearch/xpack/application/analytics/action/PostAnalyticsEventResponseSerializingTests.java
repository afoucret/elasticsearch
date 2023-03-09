/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.action;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.AbstractWireSerializingTestCase;

import java.io.IOException;

public class PostAnalyticsEventResponseSerializingTests extends AbstractWireSerializingTestCase<PostAnalyticsEventAction.Response> {

    @Override
    protected Writeable.Reader<PostAnalyticsEventAction.Response> instanceReader() {
        return PostAnalyticsEventAction.Response::new;
    }

    @Override
    protected PostAnalyticsEventAction.Response createTestInstance() {
        return new PostAnalyticsEventAction.Response(randomBoolean());
    }

    @Override
    protected PostAnalyticsEventAction.Response mutateInstance(PostAnalyticsEventAction.Response instance) throws IOException {
        return randomValueOtherThan(instance, this::createTestInstance);
    }
}
