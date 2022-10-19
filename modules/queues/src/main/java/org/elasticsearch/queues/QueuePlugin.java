/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues;

import org.apache.lucene.util.SetOnce;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.client.internal.Client;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.allocation.decider.AllocationDeciders;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.indices.SystemIndexDescriptor;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SystemIndexPlugin;
import org.elasticsearch.queues.action.GetQueueAction;
import org.elasticsearch.queues.action.GetQueueTransportAction;
import org.elasticsearch.queues.action.PutQueueAction;
import org.elasticsearch.queues.action.PutQueueTransportAction;
import org.elasticsearch.queues.rest.RestGetQueueAction;
import org.elasticsearch.queues.rest.RestPutQueueAction;
import org.elasticsearch.repositories.RepositoriesService;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.tracing.Tracer;
import org.elasticsearch.watcher.ResourceWatcherService;
import org.elasticsearch.xcontent.NamedXContentRegistry;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class QueuePlugin extends Plugin implements ActionPlugin, SystemIndexPlugin {

    private final SetOnce<QueueManagementService> queueManagementService = new SetOnce<>();

    @Override
    public Collection<Object> createComponents(Client client, ClusterService clusterService, ThreadPool threadPool, ResourceWatcherService resourceWatcherService, ScriptService scriptService, NamedXContentRegistry xContentRegistry, Environment environment, NodeEnvironment nodeEnvironment, NamedWriteableRegistry namedWriteableRegistry, IndexNameExpressionResolver indexNameExpressionResolver, Supplier<RepositoriesService> repositoriesServiceSupplier, Tracer tracer, AllocationDeciders allocationDeciders) {

        queueManagementService.set(new QueueManagementService(client));

        return List.of(queueManagementService.get());
    }


    @Override
    public Collection<SystemIndexDescriptor> getSystemIndexDescriptors(Settings unused) {
        return List.of(QueueMetadataIndex.getQueueSystemIndexDescriptor());
    }

    @Override
    public String getFeatureName() {
        return "queues";
    }

    @Override
    public String getFeatureDescription() {
        return "Provides a basic queue feature";
    }

    @Override
    public List<ActionPlugin.ActionHandler<? extends ActionRequest, ? extends ActionResponse>> getActions() {
        return List.of(
            new ActionPlugin.ActionHandler<>(PutQueueAction.INSTANCE, PutQueueTransportAction.class),
            new ActionPlugin.ActionHandler<>(GetQueueAction.INSTANCE, GetQueueTransportAction.class)
        );
    }

    @Override
    public List<RestHandler> getRestHandlers(
        Settings settings,
        RestController restController,
        ClusterSettings clusterSettings,
        IndexScopedSettings indexScopedSettings,
        SettingsFilter settingsFilter,
        IndexNameExpressionResolver indexNameExpressionResolver,
        Supplier<DiscoveryNodes> nodesInCluster
    ) {
        return List.of(new RestPutQueueAction(), new RestGetQueueAction());
    }
}
