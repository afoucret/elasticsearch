/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.ingest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.json.JsonXContent;
import org.elasticsearch.xpack.application.analytics.AnalyticsEventIngestService;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEvent;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class LogAnalyticsEventConsumer extends AbstractAnalyticsEventConsumer {
    private static final Logger logger = LogManager.getLogger(LogAnalyticsEventConsumer.class);

    private static final Marker ANALYTICS_EVENT_MARKER = MarkerManager.getMarker("ANALYTICS_EVENT");

    private final BlockingQueue<AnalyticsEvent> eventQueue;

    public LogAnalyticsEventConsumer(ThreadPool threadPool, BlockingQueue<AnalyticsEvent> eventQueue) {
        super(threadPool.executor(AnalyticsEventIngestService.THREAD_POOL_NAME));
        this.eventQueue = eventQueue;
    }

    @Override
    protected AnalyticsEvent getNextEvent() throws InterruptedException {
        return eventQueue.take();
    }

    @Override
    public void accept(AnalyticsEvent event) {
        try {
            logger.info(ANALYTICS_EVENT_MARKER, formatEvent(event));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Formats an analytics event as a JSON string.
     *
     * @param event the event to format
     * @return the formatted JSON string
     * @throws IOException if an I/O error occurs while formatting the JSON
     */
    private String formatEvent(AnalyticsEvent event) throws IOException {
        return Strings.toString(event.toXContent(JsonXContent.contentBuilder(), ToXContent.EMPTY_PARAMS));
    }

    public static class Factory {
        private final ThreadPool threadPool;

        @Inject
        public Factory(ThreadPool threadPool) {
            this.threadPool = threadPool;
        }

        public AnalyticsEventConsumer create(BlockingQueue<AnalyticsEvent> eventQueue, Settings settings) {
            return new LogAnalyticsEventConsumer(threadPool, eventQueue);
        }
    }
}
