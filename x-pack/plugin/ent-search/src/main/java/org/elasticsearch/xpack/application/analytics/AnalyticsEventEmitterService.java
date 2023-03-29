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
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.json.JsonXContent;
import org.elasticsearch.xpack.application.analytics.action.PostAnalyticsEventAction;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEvent;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEventFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Event emitter will log Analytics events submitted through a @{PostAnalyticsEventAction.Request} request.
 * Event will be emitted in using a specific logger created for the purpose of logging analytics events.
 * The log file is formatted as a ndjson file (one json per line). We send formatted JSON to the logger directly.
 */
public class AnalyticsEventEmitterService {
    private static final Logger logger = LogManager.getLogger(AnalyticsEventEmitterService.class);

    private static final Marker ANALYTICS_MARKER = MarkerManager.getMarker("org.elasticsearch.xpack.application.analytics");

    private final ClusterService clusterService;

    private final AnalyticsCollectionResolver analyticsCollectionResolver;

    private final AnalyticsEventFactory analyticsEventFactory;

    @Inject
    public AnalyticsEventEmitterService(AnalyticsCollectionResolver analyticsCollectionResolver, ClusterService clusterService) {
        this(AnalyticsEventFactory.INSTANCE, analyticsCollectionResolver, clusterService);
    }

    public AnalyticsEventEmitterService(
        AnalyticsEventFactory analyticsEventFactory,
        AnalyticsCollectionResolver analyticsCollectionResolver,
        ClusterService clusterService
    ) {
        this.analyticsEventFactory = Objects.requireNonNull(analyticsEventFactory, "analyticsEventFactory");
        this.analyticsCollectionResolver = Objects.requireNonNull(analyticsCollectionResolver, "analyticsCollectionResolver");
        this.clusterService = Objects.requireNonNull(clusterService, "clusterService");
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
            logger.info(ANALYTICS_MARKER, formatEvent(event));

            if (request.isDebug()) {
                listener.onResponse(new PostAnalyticsEventAction.DebugResponse(true, event));
            } else {
                listener.onResponse(PostAnalyticsEventAction.Response.ACCEPTED);
            }
        } catch (Exception e) {
            listener.onFailure(e);
        }
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
}
