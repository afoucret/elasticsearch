
/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */
package org.elasticsearch.contentindices.rest;

import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.contentindices.action.GetContentIndicesAction;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.GET;

public class RestGetContentIndicesAction extends BaseRestHandler {

    @Override
    public String getName() {
        return "get_content_index_action";
    }

    @Override
    public List<Route> routes() {
        return List.of(new Route(GET, "/_content_index"), new Route(GET, "/_content_index/{name}"));
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) {
        GetContentIndicesAction.Request getContentIndexRequest = new GetContentIndicesAction.Request(
            Strings.splitStringByCommaToArray(request.param("name"))
        );
        getContentIndexRequest.indicesOptions(IndicesOptions.fromRequest(request, getContentIndexRequest.indicesOptions()));
        return channel -> client.execute(GetContentIndicesAction.INSTANCE, getContentIndexRequest, new RestToXContentListener<>(channel));
    }

    @Override
    public boolean allowSystemIndexAccessByDefault() {
        return true;
    }
}
