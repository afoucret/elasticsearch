/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.ingest;

import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xpack.application.analytics.AnalyticsEventIngestService;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEvent;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class AnalyticsEventConsumerFactory {

    @FunctionalInterface
    private interface FactoryMethod {
        AnalyticsEventConsumer create(BlockingQueue<AnalyticsEvent> queue, Settings settings);
    }

    private final Map<AnalyticsEventConsumer.Type, FactoryMethod> factories;

    @Inject
    public AnalyticsEventConsumerFactory(
        ElasticsearchAnalyticsEventConsumer.Factory elasticsearchConsumerFactory,
        LogAnalyticsEventConsumer.Factory logConsumerFactory
    ) {
        factories = MapBuilder.<AnalyticsEventConsumer.Type, FactoryMethod>newMapBuilder()
            .put(AnalyticsEventConsumer.Type.ELASTICSEARCH, elasticsearchConsumerFactory::create)
            .put(AnalyticsEventConsumer.Type.LOG, logConsumerFactory::create)
            .immutableMap();
    }

    public AnalyticsEventConsumer create(BlockingQueue<AnalyticsEvent> queue, Settings settings) {
        return factories.get(AnalyticsEventIngestService.CONSUMER_TYPE_SETTING.get(settings)).create(queue, settings);
    }
}
