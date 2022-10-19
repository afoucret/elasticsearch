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
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.queues.QueueManagementService;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.transport.TransportService;

public class PutQueueTransportAction extends HandledTransportAction<PutQueueAction.Request, AcknowledgedResponse> {

    private final QueueManagementService queueManagementService;

    @Inject
    public PutQueueTransportAction(TransportService transportService, ActionFilters actionFilters, QueueManagementService queueManagementService) {
        super(PutQueueAction.NAME, transportService, actionFilters, PutQueueAction.Request::new);
        this.queueManagementService = queueManagementService;
    }

    @Override
    protected void doExecute(Task task, PutQueueAction.Request request, ActionListener<AcknowledgedResponse> listener) {
        queueManagementService.putQueue(request, listener);
    }
}
