/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues.action;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.master.AcknowledgedTransportMasterNodeAction;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.queues.QueueMetadataService;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

public class PutQueueTransportAction extends AcknowledgedTransportMasterNodeAction<PutQueueAction.Request> {

    private final QueueMetadataService queueMetadataService;

    @Inject
    public PutQueueTransportAction(
        TransportService transportService,
        ClusterService clusterService,
        ThreadPool threadPool,
        ActionFilters actionFilters,
        IndexNameExpressionResolver indexNameExpressionResolver,
        QueueMetadataService queueMetadataService
    ) {
        super(
            PutQueueAction.NAME,
            transportService,
            clusterService,
            threadPool,
            actionFilters,
            PutQueueAction.Request::new,
            indexNameExpressionResolver,
            ThreadPool.Names.SAME
        );

        this.queueMetadataService = queueMetadataService;
    }

    @Override
    protected void masterOperation(
        Task task,
        PutQueueAction.Request request,
        ClusterState state,
        ActionListener<AcknowledgedResponse> listener
    ) throws Exception {
        queueMetadataService.putQueue(request, listener);
    }

    @Override
    protected ClusterBlockException checkBlock(PutQueueAction.Request request, ClusterState state) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.METADATA_WRITE);
    }
}
