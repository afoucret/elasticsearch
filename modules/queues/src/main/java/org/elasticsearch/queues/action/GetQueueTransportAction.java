/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues.action;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.queues.QueueManagementService;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.transport.TransportService;

public class GetQueueTransportAction extends HandledTransportAction<GetQueueAction.Request, GetQueueAction.Response> {

    private final QueueManagementService queueManagementService;

    @Inject
    public GetQueueTransportAction(TransportService transportService, ActionFilters actionFilters, QueueManagementService queueManagementService) {
        super(GetQueueAction.NAME, transportService, actionFilters, GetQueueAction.Request::new);
        this.queueManagementService = queueManagementService;
    }

    @Override
    protected void doExecute(Task task, GetQueueAction.Request request, ActionListener<GetQueueAction.Response> listener) {
        queueManagementService.getQueue(request, ActionListener.wrap(
            (queue) -> { listener.onResponse(new GetQueueAction.Response(queue)); },
            listener::onFailure
        ));
    }
}
