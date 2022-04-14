/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.contentindices.rest;

import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.contentindices.action.PutContentIndexAction;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.PUT;

public class RestPutContentIndexAction  extends BaseRestHandler {

    @Override
    public String getName() {
        return "put_content_index_action";
    }

    @Override
    public List<Route> routes() {
        return List.of(new RestHandler.Route(PUT, "/_content_index/{name}"));
    }

    @Override
    protected BaseRestHandler.RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) {
        PutContentIndexAction.Request putDataStreamRequest = new PutContentIndexAction.Request(request.param("name"));
        return channel -> client.execute(PutContentIndexAction.INSTANCE, putDataStreamRequest, new RestToXContentListener<>(channel));
    }

}
