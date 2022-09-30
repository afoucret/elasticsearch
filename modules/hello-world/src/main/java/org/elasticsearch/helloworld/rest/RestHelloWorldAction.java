/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.helloworld.rest;

import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.helloworld.action.HelloWorldAction;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.GET;

public class RestHelloWorldAction extends BaseRestHandler {
    @Override
    public String getName() {
        return "get_content_index_action";
    }

    @Override
    public List<Route> routes() {
        return List.of(new Route(GET, "/_hello/{name}"));
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) {
        HelloWorldAction.Request actionRequest = new HelloWorldAction.Request(request.param("name"));
        return channel -> client.execute(HelloWorldAction.INSTANCE, actionRequest, new RestToXContentListener<>(channel));
    }
}
