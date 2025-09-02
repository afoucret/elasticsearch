/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.mcp;

public class McpSchema {

    private McpSchema() {
    }

    public sealed interface RequestId permits IntegerRequestId, StringRequestId {
        public Object id();
    }

    public record IntegerRequestId(Integer id) implements RequestId {

    }

    public record StringRequestId(Integer id) implements RequestId {

    }

    public enum Method {

    }

    public sealed interface Request permits InitializeRequest {
        public RequestId id();

        public Method method();
    }

    public record InitializeRequest(RequestId id, Method method) implements Request {

    }
}
