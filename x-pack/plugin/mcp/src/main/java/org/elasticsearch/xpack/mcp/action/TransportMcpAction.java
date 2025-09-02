/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.mcp.action;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.transport.TransportService;

public class TransportMcpAction extends HandledTransportAction<McpAction.Request, McpAction.Response> {

    public TransportMcpAction(TransportService transportService, ActionFilters actionFilters) {
        super(McpAction.NAME, transportService, actionFilters, McpAction.Request::new, EsExecutors.DIRECT_EXECUTOR_SERVICE);
    }

    @Override
    protected void doExecute(Task task, McpAction.Request request, ActionListener<McpAction.Response> listener) {
        listener.onResponse(new McpAction.Response("This is a response from the MCP plugin"));
    }
}
