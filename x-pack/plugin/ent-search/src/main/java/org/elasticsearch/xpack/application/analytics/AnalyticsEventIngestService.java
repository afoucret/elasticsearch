/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.ClusterStateListener;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.component.Lifecycle;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.gateway.GatewayService;
import org.elasticsearch.xpack.application.analytics.action.PostAnalyticsEventAction;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEvent;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEventFactory;
import org.elasticsearch.xpack.application.analytics.ingest.AnalyticsEventConsumer;
import org.elasticsearch.xpack.application.analytics.ingest.AnalyticsEventConsumerFactory;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Event emitter will log Analytics events submitted through a @{PostAnalyticsEventAction.Request} request.
 * Event will be emitted in using a specific logger created for the purpose of logging analytics events.
 * The log file is formatted as a ndjson file (one json per line). We send formatted JSON to the logger directly.
 */
public class AnalyticsEventIngestService implements ClusterStateListener {
    public static final String THREAD_POOL_NAME = "behavioral_analytics_event_ingest";
    private final ClusterService clusterService;

    private final AnalyticsCollectionResolver analyticsCollectionResolver;

    private final AnalyticsEventFactory analyticsEventFactory;

    // Queue should be instantiated from settings (max size, ...)
    private final BlockingQueue<AnalyticsEvent> eventQueue;

    private final AnalyticsEventConsumer analyticsEventConsumer;

    @Inject
    public AnalyticsEventIngestService(
        AnalyticsEventFactory analyticsEventFactory,
        AnalyticsCollectionResolver analyticsCollectionResolver,
        AnalyticsEventConsumerFactory analyticsEventConsumerFactory,
        ClusterService clusterService,
        Settings settings
    ) {
        this.eventQueue = new LinkedBlockingQueue<>();
        this.analyticsEventFactory = Objects.requireNonNull(analyticsEventFactory, "analyticsEventFactory");
        this.analyticsCollectionResolver = Objects.requireNonNull(analyticsCollectionResolver, "analyticsCollectionResolver");
        this.clusterService = Objects.requireNonNull(clusterService, "clusterService");
        this.analyticsEventConsumer = analyticsEventConsumerFactory.create(eventQueue, settings);

        clusterService.addListener(this);
    }

    @Override
    public void clusterChanged(ClusterChangedEvent event) {
        ClusterState state = event.state();
        if (state.blocks().hasGlobalBlock(GatewayService.STATE_NOT_RECOVERED_BLOCK)) {
            // wait until the gateway has recovered from disk before starting our ingest service
            return;
        }

        if(analyticsEventConsumer.lifecycleState() == Lifecycle.State.INITIALIZED) {
            this.analyticsEventConsumer.start();
        }
    }

    /**
     * Logs an analytics event.
     *
     * @param request the request containing the analytics event data
     * @param listener the listener to call once the event has been emitted
     */
    public void postEvent(PostAnalyticsEventAction.Request request, ActionListener<PostAnalyticsEventAction.Response> listener) {
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
}
