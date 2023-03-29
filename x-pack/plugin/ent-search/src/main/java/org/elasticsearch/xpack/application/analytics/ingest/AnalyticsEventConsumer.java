/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.ingest;

import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEvent;

import java.util.function.Consumer;

public interface AnalyticsEventConsumer extends Consumer<AnalyticsEvent>, LifecycleComponent {
    enum Type {
        LOG,
        ELASTICSEARCH;
    }
}
