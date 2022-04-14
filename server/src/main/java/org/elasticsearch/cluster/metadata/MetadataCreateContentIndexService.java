/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */
package org.elasticsearch.cluster.metadata;

import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexClusterStateUpdateRequest;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.ActiveShardsObserver;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.cluster.AckedClusterStateUpdateTask;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.ClusterStateTaskExecutor;
import org.elasticsearch.cluster.ClusterStateUpdateTask;
import org.elasticsearch.cluster.ack.ClusterStateUpdateRequest;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.Priority;
import org.elasticsearch.core.SuppressForbidden;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.Index;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class MetadataCreateContentIndexService {

    private final ClusterService clusterService;
    private final ActiveShardsObserver activeShardsObserver;
    private final MetadataCreateIndexService metadataCreateIndexService;

    public MetadataCreateContentIndexService(
        ThreadPool threadPool,
        ClusterService clusterService,
        MetadataCreateIndexService metadataCreateIndexService
    ) {
        this.clusterService = clusterService;
        this.activeShardsObserver = new ActiveShardsObserver(clusterService, threadPool);
        this.metadataCreateIndexService = metadataCreateIndexService;
    }

    public void createContentIndex(
        CreateContentIndexClusterStateUpdateRequest request,
        ActionListener<AcknowledgedResponse> finalListener
    ) {
        AtomicReference<String> firstBackingIndexRef = new AtomicReference<>();
        ActionListener<AcknowledgedResponse> listener = ActionListener.wrap(response -> {
            if (response.isAcknowledged()) {
                String firstBackingIndexName = firstBackingIndexRef.get();
                assert firstBackingIndexName != null;
                activeShardsObserver.waitForActiveShards(
                    new String[] { firstBackingIndexName },
                    ActiveShardCount.DEFAULT,
                    request.masterNodeTimeout(),
                    shardsAcked -> finalListener.onResponse(AcknowledgedResponse.TRUE),
                    finalListener::onFailure
                );
            } else {
                finalListener.onResponse(AcknowledgedResponse.FALSE);
            }
        }, finalListener::onFailure);
        clusterService.submitStateUpdateTask(
            "create-content-index [" + request.name + "]",
            new AckedClusterStateUpdateTask(Priority.HIGH, request, listener) {
                @Override
                public ClusterState execute(ClusterState currentState) throws Exception {
                    ClusterState clusterState = createContentIndex(currentState, metadataCreateIndexService, request);
                    firstBackingIndexRef.set(clusterState.metadata().contentIndices().get(request.name).getIndex().getName());
                    return clusterState;
                }
            },
            newExecutor()
        );
    }

    @SuppressForbidden(reason = "legacy usage of unbatched task") // TODO add support for batching here
    private static <T extends ClusterStateUpdateTask> ClusterStateTaskExecutor<T> newExecutor() {
        return ClusterStateTaskExecutor.unbatched();
    }

    public static final class CreateContentIndexClusterStateUpdateRequest extends ClusterStateUpdateRequest<
        CreateContentIndexClusterStateUpdateRequest> {

        private final boolean performReroute;
        private final String name;
        private final long startTime;

        public CreateContentIndexClusterStateUpdateRequest(String name) {
            this(name, System.currentTimeMillis(), TimeValue.ZERO, TimeValue.ZERO, true);
        }

        public CreateContentIndexClusterStateUpdateRequest(
            String name,
            TimeValue masterNodeTimeout,
            TimeValue timeout,
            boolean performReroute
        ) {
            this(name, System.currentTimeMillis(), masterNodeTimeout, timeout, performReroute);
        }

        public CreateContentIndexClusterStateUpdateRequest(
            String name,
            long startTime,
            TimeValue masterNodeTimeout,
            TimeValue timeout,
            boolean performReroute
        ) {
            this.name = name;
            this.startTime = startTime;
            this.performReroute = performReroute;
            masterNodeTimeout(masterNodeTimeout);
            ackTimeout(timeout);
        }

        public boolean performReroute() {
            return performReroute;
        }
    }

    /**
     * Creates a data stream with the specified request, backing indices and write index.
     *
     * @param currentState               Cluster state
     * @param request                    The create data stream request
     * @return                           Cluster state containing the new data stream
     */
    static ClusterState createContentIndex(
        ClusterState currentState,
        MetadataCreateIndexService metadataCreateIndexService,
        CreateContentIndexClusterStateUpdateRequest request
    ) throws Exception {
        String contentIndexName = request.name;

        Objects.requireNonNull(currentState);

        if (currentState.metadata().dataStreams().containsKey(contentIndexName)) {
            throw new ResourceAlreadyExistsException("data_stream [" + contentIndexName + "] already exists");
        }

        if (currentState.metadata().contentIndices().containsKey(contentIndexName)) {
            throw new ResourceAlreadyExistsException("content_index [" + contentIndexName + "] already exists");
        }

        MetadataCreateIndexService.validateIndexOrAliasName(
            contentIndexName,
            (s1, s2) -> new IllegalArgumentException("content_index [" + s1 + "] " + s2)
        );

        if (contentIndexName.toLowerCase(Locale.ROOT).equals(contentIndexName) == false) {
            throw new IllegalArgumentException("content_index [" + contentIndexName + "] must be lowercase");
        }
        if (contentIndexName.startsWith(DataStream.BACKING_INDEX_PREFIX)) {
            throw new IllegalArgumentException(
                "content_index [" + contentIndexName + "] must not start with '" + DataStream.BACKING_INDEX_PREFIX + "'"
            );
        }

        String backingIndexName = ".content-index-" + contentIndexName;

        CreateIndexClusterStateUpdateRequest createIndexRequest = new CreateIndexClusterStateUpdateRequest(
            "initialize_content_index",
            backingIndexName,
            backingIndexName
        );

        try {
            currentState = metadataCreateIndexService.applyCreateIndexRequest(currentState, createIndexRequest, false);
        } catch (ResourceAlreadyExistsException e) {
            // Rethrow as ElasticsearchStatusException, so that bulk transport action doesn't ignore it during
            // auto index/data stream creation.
            // (otherwise bulk execution fails later, because data stream will also not have been created)
            throw new ElasticsearchStatusException(
                "content index could not be created because backing index [{}] already exists",
                RestStatus.BAD_REQUEST,
                e,
                backingIndexName
            );
        }

        Index index = currentState.metadata().index(backingIndexName).getIndex();
        ContentIndex newContentIndex = new ContentIndex(contentIndexName, index);

        Metadata.Builder builder = Metadata.builder(currentState.metadata()).put(newContentIndex);

        return ClusterState.builder(currentState).metadata(builder).build();
    }

}
