/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues.action;

import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.ActionType;
import org.elasticsearch.action.ValidateActions;
import org.elasticsearch.action.support.master.AcknowledgedRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.xcontent.ObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;
import java.util.Objects;

public class PutQueueAction extends ActionType<AcknowledgedResponse> {
    public static final PutQueueAction INSTANCE = new PutQueueAction();
    public static final String NAME = "cluster:admin/queue/put";

    private PutQueueAction() {
        super(NAME, AcknowledgedResponse::readFrom);
    }

    public static class Request extends AcknowledgedRequest<Request> {

        private final String name;
        private final int size;

        public static Request parseRestRequest(RestRequest restRequest) throws IOException {
            final RequestBuilder request = new RequestBuilder(restRequest.param("name"));
            XContentParser contentParser = restRequest.contentParser();
            RequestBuilder.PARSER.parse(contentParser, request, restRequest);
            return request.build();
        }

        public Request(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public Request(StreamInput in) throws IOException {
            this.name = in.readString();
            this.size = in.readInt();
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeString(name);
            out.writeInt(size);
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
            return size == request.size && Objects.equals(name, request.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, size);
        }

        public static class RequestBuilder {

            private static final ObjectParser<RequestBuilder, RestRequest> PARSER;
            static {
                PARSER = new ObjectParser<>("put_queue_request");
                PARSER.declareInt(RequestBuilder::setSize, new ParseField("size"));
            }

            private static final int DEFAULT_SIZE = 10;

            private String name;
            private int size = DEFAULT_SIZE;

            public RequestBuilder(String name) {
                this.name = name;
            }

            public Request build() {
                return new Request(name, size);
            }

            public void setSize(int size) {
                this.size = size;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                RequestBuilder that = (RequestBuilder) o;
                return size == that.size && Objects.equals(name, that.name);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, size);
            }
        }
    }
}
