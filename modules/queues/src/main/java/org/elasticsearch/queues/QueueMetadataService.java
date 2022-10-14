/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues;

import org.elasticsearch.ResourceNotFoundException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.ClusterStateTaskConfig;
import org.elasticsearch.cluster.ClusterStateTaskExecutor;
import org.elasticsearch.cluster.ClusterStateTaskListener;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.queues.action.GetQueueAction;
import org.elasticsearch.queues.action.PutQueueAction;
import org.elasticsearch.queues.metadata.Queue;
import org.elasticsearch.queues.metadata.QueueMetadata;

public class QueueMetadataService {

    private final ClusterService clusterService;

    public QueueMetadataService(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    static final ClusterStateTaskExecutor<ClusterStateUpdateTask> TASK_EXECUTOR = batchExecutionContext -> {
        final QueueMetadata initialMetadata = batchExecutionContext.initialState()
            .metadata()
            .custom(QueueMetadata.TYPE, QueueMetadata.EMPTY);
        var currentMetadata = initialMetadata;
        for (final var taskContext : batchExecutionContext.taskContexts()) {
            try {
                final var task = taskContext.getTask();
                try (var ignored = taskContext.captureResponseHeaders()) {
                    currentMetadata = task.execute(currentMetadata);
                }
                taskContext.success(() -> task.listener.onResponse(AcknowledgedResponse.TRUE));
            } catch (Exception e) {
                taskContext.onFailure(e);
            }
        }
        final var finalMetadata = currentMetadata;
        return finalMetadata == initialMetadata
            ? batchExecutionContext.initialState()
            : batchExecutionContext.initialState().copyAndUpdateMetadata(b -> { b.putCustom(QueueMetadata.TYPE, finalMetadata); });
    };

    public void putQueue(PutQueueAction.Request request, ActionListener<AcknowledgedResponse> listener) {
        clusterService.submitStateUpdateTask(
            "put-queue-engine-" + request.getName(),
            new PutQueueClusterStateUpdateTask(listener, request),
            ClusterStateTaskConfig.build(Priority.NORMAL, request.masterNodeTimeout()),
            TASK_EXECUTOR
        );
    }

    public void getQueue(ClusterState clusterState, GetQueueAction.Request request, ActionListener<GetQueueAction.Response> listener) {
        QueueMetadata queueMetadata = clusterState.metadata().custom(QueueMetadata.TYPE, QueueMetadata.EMPTY);
        if (queueMetadata.getQueues().containsKey(request.getName())) {
            listener.onResponse(new GetQueueAction.Response(queueMetadata.getQueues().get(request.getName())));
        } else {
            listener.onFailure(new ResourceNotFoundException("Queue not found: " + request.getName()));
        }
    }

    static class PutQueueClusterStateUpdateTask extends ClusterStateUpdateTask {
        private final PutQueueAction.Request request;

        PutQueueClusterStateUpdateTask(ActionListener<AcknowledgedResponse> listener, PutQueueAction.Request request) {
            super(listener);
            this.request = request;
        }

        @Override
        public QueueMetadata execute(QueueMetadata currentMetadata) {
            ImmutableOpenMap.Builder<String, Queue> queues = ImmutableOpenMap.builder(currentMetadata.getQueues());
            queues.put(request.getName(), new Queue(request.getName(), request.getSize()));
            return new QueueMetadata(queues.build());
        }
    }

    abstract static class ClusterStateUpdateTask implements ClusterStateTaskListener {
        final ActionListener<AcknowledgedResponse> listener;

        ClusterStateUpdateTask(ActionListener<AcknowledgedResponse> listener) {
            this.listener = listener;
        }

        public abstract QueueMetadata execute(QueueMetadata currentMetadata);

        @Override
        public void onFailure(Exception e) {
            listener.onFailure(e);
        }
    }
}



