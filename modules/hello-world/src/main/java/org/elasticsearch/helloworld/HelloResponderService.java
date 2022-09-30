/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.helloworld;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.helloworld.action.HelloWorldAction;

public class HelloResponderService {

    private final ClusterService clusterService;

    public HelloResponderService(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    public void respond(HelloWorldAction.Request request, ActionListener<HelloWorldAction.Response> listener) {
        String message = "Hey " + request.getName() + "! Hello from node " + clusterService.getNodeName();
        listener.onResponse(new HelloWorldAction.Response(message));
    }
}
