/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.contentindices.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.TransportMasterNodeReadAction;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.metadata.ContentIndex;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.indices.SystemIndices;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetContentIndicesTransportAction extends TransportMasterNodeReadAction<
    GetContentIndicesAction.Request,
    GetContentIndicesAction.Response> {

    private static final Logger LOGGER = LogManager.getLogger(GetContentIndicesTransportAction.class);

    @Inject
    public GetContentIndicesTransportAction(
        TransportService transportService,
        ClusterService clusterService,
        ThreadPool threadPool,
        ActionFilters actionFilters,
        IndexNameExpressionResolver indexNameExpressionResolver
    ) {
        super(
            GetContentIndicesAction.NAME,
            transportService,
            clusterService,
            threadPool,
            actionFilters,
            GetContentIndicesAction.Request::new,
            indexNameExpressionResolver,
            GetContentIndicesAction.Response::new,
            ThreadPool.Names.SAME
        );
    }


    @Override
    protected void masterOperation(
        Task task,
        GetContentIndicesAction.Request request,
        ClusterState state,
        ActionListener<GetContentIndicesAction.Response> listener) {
        List<ContentIndex> contentIndices = getContentIndices(request, state);
        Map<String, Map<String, String>> mappings = new HashMap<>();
        listener.onResponse(new GetContentIndicesAction.Response(contentIndices));
    }

    private List<ContentIndex> getContentIndices(GetContentIndicesAction.Request request, ClusterState clusterState) {
        List<String> results = getContentIndicesmNames(
            indexNameExpressionResolver,
            clusterState,
            request.getNames(),
            request.indicesOptions()
        );
        Map<String, ContentIndex> contentIndices = clusterState.metadata().contentIndices();

        return results.stream().map(contentIndices::get).sorted(Comparator.comparing(ContentIndex::getName)).toList();
    }

    @Override
    protected ClusterBlockException checkBlock(GetContentIndicesAction.Request request, ClusterState state) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.METADATA_WRITE);
    }

    private static List<String> getContentIndicesmNames(
        IndexNameExpressionResolver indexNameExpressionResolver,
        ClusterState currentState,
        String[] names,
        IndicesOptions indicesOptions
    ) {
        indicesOptions = updateIndicesOptions(indicesOptions);
        return indexNameExpressionResolver.contentIndexNames(currentState, indicesOptions, names);
    }

    private static IndicesOptions updateIndicesOptions(IndicesOptions indicesOptions) {
        EnumSet<IndicesOptions.WildcardStates> expandWildcards = indicesOptions.expandWildcards();
        expandWildcards.add(IndicesOptions.WildcardStates.OPEN);
        indicesOptions = new IndicesOptions(indicesOptions.options(), expandWildcards);

        return indicesOptions;
    }
}
