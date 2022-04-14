/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.contentindices.action;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.master.AcknowledgedTransportMasterNodeAction;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.metadata.MetadataCreateContentIndexService;
import org.elasticsearch.cluster.metadata.MetadataCreateContentIndexService.CreateContentIndexClusterStateUpdateRequest;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

public class PutContentIndexTransportAction extends AcknowledgedTransportMasterNodeAction<PutContentIndexAction.Request> {

    private final MetadataCreateContentIndexService createContentIndexService;

    @Inject
    public PutContentIndexTransportAction(
        TransportService transportService,
        ClusterService clusterService,
        ThreadPool threadPool,
        ActionFilters actionFilters,
        IndexNameExpressionResolver indexNameExpressionResolver,
        MetadataCreateContentIndexService createContentIndexService
    ) {
        super(
            PutContentIndexAction.NAME,
            transportService,
            clusterService,
            threadPool,
            actionFilters,
            PutContentIndexAction.Request::new,
            indexNameExpressionResolver,
            ThreadPool.Names.SAME
        );

        this.createContentIndexService = createContentIndexService;
    }

    @Override
    protected void masterOperation(
        Task task,
        PutContentIndexAction.Request request,
        ClusterState state,
        ActionListener<AcknowledgedResponse> listener
    )  {
        CreateContentIndexClusterStateUpdateRequest createContentIndexRequest = new CreateContentIndexClusterStateUpdateRequest(
            request.getName(),
            request.masterNodeTimeout(),
            request.timeout(),
            true
        );
        createContentIndexService.createContentIndex(createContentIndexRequest, listener);
    }

    @Override
    protected ClusterBlockException checkBlock(PutContentIndexAction.Request request, ClusterState state) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.METADATA_WRITE);
    }
}
