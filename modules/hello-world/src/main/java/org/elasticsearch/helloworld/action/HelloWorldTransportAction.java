/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.helloworld.action;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.helloworld.HelloResponderService;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.transport.TransportService;

public class HelloWorldTransportAction extends HandledTransportAction<HelloWorldAction.Request, HelloWorldAction.Response> {

    private final HelloResponderService responder;

    @Inject
    public HelloWorldTransportAction(TransportService transportService, ActionFilters actionFilters, HelloResponderService responder) {
        super(HelloWorldAction.NAME, transportService, actionFilters, HelloWorldAction.Request::new);
        this.responder = responder;
    }

    @Override
    protected void doExecute(Task task, HelloWorldAction.Request request, ActionListener<HelloWorldAction.Response> listener) {
        responder.respond(request, listener);
    }
}
