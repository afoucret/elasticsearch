/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.LoggerMessageFormat;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.json.JsonXContent;
import org.elasticsearch.xpack.application.analytics.action.PostAnalyticsEventAction;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEvent;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEventFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Event emitter will log Analytics events submitted through a @{PostAnalyticsEventAction.Request} request.
 * Event will be emitted in using a specific logger created for the purpose of logging analytics events.
 * The log file is formatted as a ndjson file (one json per line). We send formatted JSON to the logger directly.
 */
public class AnalyticsEventIngestService {
    public static final String THREAD_POOL_NAME = "behavioral_analytics_event_ingest";
    private final ClusterService clusterService;

    private final AnalyticsCollectionResolver analyticsCollectionResolver;

    private final AnalyticsEventFactory analyticsEventFactory;

    private final BlockingQueue<AnalyticsEvent> eventQueue = new LinkedBlockingQueue<>();

    private final EventConsumer eventConsumer;

    @Inject
    public AnalyticsEventIngestService(AnalyticsCollectionResolver analyticsCollectionResolver, ClusterService clusterService, ThreadPool threadPool) {
        this(AnalyticsEventFactory.INSTANCE, analyticsCollectionResolver, clusterService, threadPool);
    }

    public AnalyticsEventIngestService(
        AnalyticsEventFactory analyticsEventFactory,
        AnalyticsCollectionResolver analyticsCollectionResolver,
        ClusterService clusterService,
        ThreadPool threadPool
    ) {
        this.analyticsEventFactory = Objects.requireNonNull(analyticsEventFactory, "analyticsEventFactory");
        this.analyticsCollectionResolver = Objects.requireNonNull(analyticsCollectionResolver, "analyticsCollectionResolver");
        this.clusterService = Objects.requireNonNull(clusterService, "clusterService");
        EventConsumerFactory eventConsumerFactory = LogEventConsumer.Factory::create;
        this.eventConsumer = eventConsumerFactory.create(threadPool, eventQueue);

        // Should not be here.
        this.eventConsumer.start();
    }

    /**
     * Logs an analytics event.
     *
     * @param request the request containing the analytics event data
     * @param listener the listener to call once the event has been emitted
     */
    public void emitEvent(
        final PostAnalyticsEventAction.Request request,
        final ActionListener<PostAnalyticsEventAction.Response> listener
    ) {
        try {
            analyticsCollectionResolver.collection(clusterService.state(), request.eventCollectionName());
            AnalyticsEvent event = analyticsEventFactory.fromRequest(request);

            eventQueue.add(event);

            if (request.isDebug()) {
                listener.onResponse(new PostAnalyticsEventAction.DebugResponse(true, event));
            } else {
                listener.onResponse(PostAnalyticsEventAction.Response.ACCEPTED);
            }
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    interface EventConsumer {
        void start();

        void stop();
    }

    @FunctionalInterface
    interface EventConsumerFactory {
        EventConsumer create(ThreadPool threadPool, BlockingQueue<AnalyticsEvent> eventQueue);
    }

    public static class LogEventConsumer implements EventConsumer {
        private static final Logger logger = LogManager.getLogger(AnalyticsEventIngestService.LogEventConsumer.class);

        private static final Marker ANALYTICS_EVENT_MARKER = MarkerManager.getMarker("ANALYTICS_EVENT");

        private final BlockingQueue<AnalyticsEvent> eventQueue;

        private final ExecutorService executor;

        private volatile boolean running = false;

        public LogEventConsumer(ExecutorService executor, BlockingQueue<AnalyticsEvent> eventQueue) {
            this.eventQueue = eventQueue;
            this.executor = executor;
        }

        @Override
        public void start() {
            this.running = true;
            this.executor.execute(() -> {
                logger.info(LoggerMessageFormat.format(
                    null, "analytics event consumer [{}] is up and running (tid[{}])",
                    this.getClass().getName(),
                    Thread.currentThread().getId()
                ));
                while (this.running) {
                    try {
                        logger.info(ANALYTICS_EVENT_MARKER, formatEvent(eventQueue.take()));
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });
        }

        @Override
        public void stop() {
            this.running = false;
            this.executor.shutdown();
        }

        /**
         * Formats an analytics event as a JSON string.
         *
         * @param event the event to format
         *
         * @return the formatted JSON string
         *
         * @throws IOException if an I/O error occurs while formatting the JSON
         */
        private String formatEvent(AnalyticsEvent event) throws IOException {
            return Strings.toString(event.toXContent(JsonXContent.contentBuilder(), ToXContent.EMPTY_PARAMS));
        }

        public static class Factory {
            public static EventConsumer create(ThreadPool threadPool, BlockingQueue<AnalyticsEvent> eventQueue) {
                return new LogEventConsumer(threadPool.executor(THREAD_POOL_NAME), eventQueue);
            }
        }
    }
}
