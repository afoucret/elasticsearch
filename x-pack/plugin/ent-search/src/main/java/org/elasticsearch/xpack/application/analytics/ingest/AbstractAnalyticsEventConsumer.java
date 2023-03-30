/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.ingest;

import org.elasticsearch.common.component.Lifecycle;
import org.elasticsearch.common.component.LifecycleListener;
import org.elasticsearch.common.logging.LoggerMessageFormat;
import org.elasticsearch.logging.LogManager;
import org.elasticsearch.logging.Logger;
import org.elasticsearch.xpack.application.analytics.event.AnalyticsEvent;

import java.util.concurrent.ExecutorService;

public abstract class AbstractAnalyticsEventConsumer implements AnalyticsEventConsumer {
    private static final Logger logger = LogManager.getLogger(AnalyticsEventConsumer.class);

    private final ExecutorService executor;

    private Lifecycle.State state = Lifecycle.State.INITIALIZED;

    public AbstractAnalyticsEventConsumer(ExecutorService executor) {
        this.executor = executor;
    }

    protected abstract AnalyticsEvent getNextEvent() throws InterruptedException;

    protected void flush() { }

    @Override
    public void start() {
        state = Lifecycle.State.STARTED;

        this.executor.execute(() -> {
            logger.info(LoggerMessageFormat.format(
                null, "analytics event consumer [{}] is up and running (tid[{}])",
                this.getClass().getName(),
                Thread.currentThread().getId()
            ));
            while (this.state == Lifecycle.State.STARTED) {
                try {
                    accept(getNextEvent());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public void close() {
        state = Lifecycle.State.CLOSED;
    }

    @Override
    public void stop() {
        flush();
        state = Lifecycle.State.STOPPED;
    }


    @Override
    public Lifecycle.State lifecycleState() {
        return state;
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        // TODO
    }
}
