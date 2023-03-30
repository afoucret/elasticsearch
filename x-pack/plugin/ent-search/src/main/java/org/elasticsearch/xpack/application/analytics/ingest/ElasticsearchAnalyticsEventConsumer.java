/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.ingest;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.internal.Client;
import org.elasticsearch.client.internal.OriginSettingClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.logging.LogManager;
import org.elasticsearch.logging.Logger;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.elasticsearch.xcontent.XContentType;
import org.elasticsearch.xpack.application.analytics.AnalyticsCollection;
import org.elasticsearch.xpack.application.analytics.AnalyticsEventIngestService;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.xpack.core.ClientHelper.ENT_SEARCH_ORIGIN;

public class ElasticsearchAnalyticsEventConsumer extends AbstractAnalyticsEventConsumer {
    private static final Logger logger = LogManager.getLogger(ElasticsearchAnalyticsEventConsumer.class);

    private final Client client;

    private final BlockingQueue<AnalyticsEvent> eventQueue;
    private volatile BulkRequestBuilder bulkRequestBuilder;

    public ElasticsearchAnalyticsEventConsumer(Client client, ThreadPool threadPool, BlockingQueue<AnalyticsEvent> eventQueue) {
        super(threadPool.executor(AnalyticsEventIngestService.THREAD_POOL_NAME));
        this.client = client;
        this.eventQueue = eventQueue;
        this.bulkRequestBuilder = client.prepareBulk();
    }

    @Override
    protected AnalyticsEvent getNextEvent() throws InterruptedException {
        synchronized (this) {
            /*
             * Using poll with a timeout ensure we have the ability to flush events to ES
             * at a regular interval.
             *
             * TODO: make it configurable in settings.
             */
            AnalyticsEvent event = eventQueue.poll(1, TimeUnit.SECONDS);

            if (Objects.isNull(event)) {
                // No event has been received since more than 1 s.
                // Flushing the data to ES and waiting for new event (blocking)
                flush();
                event = eventQueue.take();
            }

            return event;
        }
    }

    @Override
    public void accept(AnalyticsEvent event) {
        try {
            bulkRequestBuilder.add(toIndexRequest(event));

            if (bulkRequestBuilder.numberOfActions() > 100) {
                // Flush the ES bulk if size > 100
                // TODO: it is a very conservative number.
                // TODO: Make it configurable.
                flush();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected void flush() {
        if (bulkRequestBuilder.numberOfActions() < 1) {
            return;
        }

        // Execute the bulk operation.
        ActionListener<BulkResponse> bulkResponseListener = ActionListener.wrap(
            r -> {
                if (r.hasFailures()) {
                    // TODO: parse and report errors.
                }
            },
            e -> logger.error("unable to execute bulk event create request", e)
        );

        synchronized (this) {
            BulkRequestBuilder currentBulk = bulkRequestBuilder;
            bulkRequestBuilder = client.prepareBulk();
            currentBulk.execute(bulkResponseListener);
        }
    }

    private IndexRequest toIndexRequest(AnalyticsEvent event) throws IOException {
        XContentType xContentType = XContentType.JSON;

        try (XContentBuilder sourceBuilder = event.toXContent(XContentFactory.contentBuilder(xContentType), ToXContent.EMPTY_PARAMS)) {
            AnalyticsCollection collection = new AnalyticsCollection(event.eventCollectionName());
            return client.prepareIndex(collection.getEventDataStream()).setSource(sourceBuilder).setCreate(true).request();
        }
    }

    public static class Factory {
        private final Client client;

        private final ThreadPool threadPool;

        @Inject
        public Factory(Client client, ThreadPool threadPool) {
            this.client = new OriginSettingClient(client, ENT_SEARCH_ORIGIN);
            this.threadPool = threadPool;
        }

        public AnalyticsEventConsumer create(BlockingQueue<AnalyticsEvent> eventQueue, Settings settings) {
            return new ElasticsearchAnalyticsEventConsumer(client, threadPool, eventQueue);
        }
    }
}
