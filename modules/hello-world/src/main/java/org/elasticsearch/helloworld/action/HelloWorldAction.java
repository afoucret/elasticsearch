/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.helloworld.action;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.ActionType;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Objects;

public class HelloWorldAction extends ActionType<HelloWorldAction.Response> {

    public static final HelloWorldAction INSTANCE = new HelloWorldAction();
    public static final String NAME = "cluster:hello_world";

    private HelloWorldAction() {
        super(NAME, Response::new);
    }

    public static class Request extends ActionRequest {
        private final String name;

        public Request(String name) {
            this.name = name;
        }

        public Request(StreamInput in) throws IOException {
            this(in.readString());
        }

        public String getName() {
            return name;
        }

        @Override
        public ActionRequestValidationException validate() {
            return null;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeString(name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HelloWorldAction.Request request = (HelloWorldAction.Request) o;
            return name.equals(request.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    public static class Response extends ActionResponse implements ToXContentObject {

        public static final ParseField MESSAGE_FIELD = new ParseField("message");
        private final String name;

        public Response(String name) {
            this.name = name;
        }

        public Response(StreamInput in) throws IOException {
            this(in.readString());
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeString(name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HelloWorldAction.Response response = (HelloWorldAction.Response) o;
            return name.equals(response.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
            builder.startObject();
            builder.field(MESSAGE_FIELD.getPreferredName(), getMessage());
            builder.endObject();
            return builder;
        }

        private String getMessage() {
            return "Hello, " + name + "!";
        }
    }
}
