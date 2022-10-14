/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues.rest;
import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.queues.action.PutQueueAction;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.PUT;

public class RestPutQueueAction extends BaseRestHandler{

    @Override
    public String getName() {
        return "put_search_engine_action";
    }

    @Override
    public List<Route> routes() {
        return List.of(new RestHandler.Route(PUT, "_queue/{name}"));
    }

    @Override
    protected BaseRestHandler.RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        PutQueueAction.Request createEngineRequest = PutQueueAction.Request.parseRestRequest(request);
        return channel -> client.execute(PutQueueAction.INSTANCE, createEngineRequest, new RestToXContentListener<>(channel));
    }
}
