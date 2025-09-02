/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.mcp.rest;

import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;
import org.elasticsearch.xpack.mcp.action.McpAction;

import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.GET;

public class RestMcpAction extends BaseRestHandler {

    @Override
    public String getName() {
        return "mcp_action";
    }

    @Override
    public List<Route> routes() {
        return List.of(new Route(GET, "/_mcp"));
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, final NodeClient client) {
        return channel -> client.execute(McpAction.INSTANCE, new McpAction.Request(), new RestToXContentListener<>(channel));
    }
}
