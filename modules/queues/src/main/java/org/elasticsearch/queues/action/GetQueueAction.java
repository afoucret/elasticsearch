/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues.action;

import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.ActionType;
import org.elasticsearch.action.ValidateActions;
import org.elasticsearch.action.support.master.AcknowledgedRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.master.MasterNodeReadRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.queues.metadata.Queue;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.xcontent.ObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GetQueueAction extends ActionType<GetQueueAction.Response> {
    public static final GetQueueAction INSTANCE = new GetQueueAction();
    public static final String NAME = "cluster:admin/queue/get";

    private GetQueueAction() {
        super(NAME, Response::new);
    }

    public static class Request extends MasterNodeReadRequest<Request> {

        private final String name;

        public Request(String name) {
            this.name = name;
        }

        public Request(StreamInput in) throws IOException {
            this.name = in.readString();
        }

        public String getName() {
            return name;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeString(name);
        }

        @Override
        public ActionRequestValidationException validate() {
            ActionRequestValidationException validationException = null;

            if (Strings.hasText(name) == false) {
                validationException = ValidateActions.addValidationError("name is missing", validationException);
            }

            return validationException;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Request request = (Request) o;
            return Objects.equals(name, request.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    public static class Response extends ActionResponse implements ToXContentObject {

        public static final ParseField NAME_FIELD = new ParseField("name");
        public static final ParseField SIZE_FIELD = new ParseField("size");


        private final Queue queue;

        public Response(Queue queue) {
            this.queue = queue;
        }

        public Response(StreamInput in) throws IOException {
            this.queue = new Queue(in);
        }

        public Queue getQueue() {
            return queue;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            queue.writeTo(out);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Response response = (Response) o;
            return queue.equals(response.queue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(queue);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {

            builder.startObject();
            builder.field(NAME_FIELD.getPreferredName(), queue.getName());
            builder.field(SIZE_FIELD.getPreferredName(), queue.getSize());
            builder.endObject();

            return builder;
        }
    }
}
