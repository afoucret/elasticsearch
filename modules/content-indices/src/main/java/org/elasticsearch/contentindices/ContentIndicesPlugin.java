/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */
package org.elasticsearch.contentindices;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.contentindices.action.GetContentIndicesAction;
import org.elasticsearch.contentindices.action.GetContentIndicesTransportAction;
import org.elasticsearch.contentindices.action.PutContentIndexAction;
import org.elasticsearch.contentindices.action.PutContentIndexTransportAction;
import org.elasticsearch.contentindices.rest.RestGetContentIndicesAction;
import org.elasticsearch.contentindices.rest.RestPutContentIndexAction;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;
import java.util.List;
import java.util.function.Supplier;

public class ContentIndicesPlugin extends Plugin implements ActionPlugin {

    @Override
    public List<ActionPlugin.ActionHandler<? extends ActionRequest, ? extends ActionResponse>> getActions() {
        return List.of(
            new ActionPlugin.ActionHandler<>(PutContentIndexAction.INSTANCE, PutContentIndexTransportAction.class),
            new ActionPlugin.ActionHandler<>(GetContentIndicesAction.INSTANCE, GetContentIndicesTransportAction.class)
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
        return List.of(
            new RestPutContentIndexAction(),
            new RestGetContentIndicesAction()
        );
    }

}
