/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetAction;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.index.IndexAction;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.internal.Client;
import org.elasticsearch.client.internal.OriginSettingClient;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.queues.action.PutQueueAction;
import org.elasticsearch.queues.action.GetQueueAction;
import org.elasticsearch.xcontent.XContentFactory;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xcontent.XContentParserConfiguration;
import org.elasticsearch.xcontent.XContentType;

import java.io.InputStream;


public class QueueManagementService {

    private static final Logger LOGGER = LogManager.getLogger(QueueManagementService.class);

    private final Client client;

    public QueueManagementService(Client client) {
        this.client = new OriginSettingClient(client, QueueMetadataIndex.QUEUE_ORIGIN);
    }

    public void putQueue(PutQueueAction.Request request, ActionListener<AcknowledgedResponse> listener) {
        IndexRequestBuilder indexRequest = new IndexRequestBuilder(client, IndexAction.INSTANCE, QueueMetadataIndex.indexName());
        Queue queue = new Queue(request.getName(), request.getSize());

        indexRequest.setId(request.getName())
            .setSource("name", request.getName(), "size", request.getSize())
            .execute(ActionListener.wrap(
                (indexResponse) -> listener.onResponse(AcknowledgedResponse.TRUE),
                (e) -> {
                    LOGGER.error(e);
                    listener.onFailure(e);
                }
            ));

    }

    public void getQueue(GetQueueAction.Request request, ActionListener<Queue> listener) {
        GetRequestBuilder getRequest = new GetRequestBuilder(client, GetAction.INSTANCE, QueueMetadataIndex.indexName());
        getRequest.setId(request.getName())
            .execute(ActionListener.wrap(
                (response) -> {
                    BytesReference docSource = response.getSourceAsBytesRef();
                    try (
                        InputStream stream = docSource.streamInput();
                        XContentParser parser = XContentFactory.xContent(XContentType.JSON).createParser(XContentParserConfiguration.EMPTY, stream);
                    ) {
                        listener.onResponse(Queue.fromXContent(parser));
                    } catch (Exception e) {
                        listener.onFailure(e);
                    }
                },
                listener::onFailure
            ));
    }
}
