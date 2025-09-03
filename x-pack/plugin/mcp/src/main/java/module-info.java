/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

module org.elasticsearch.mcp {
    requires org.elasticsearch.base;
    requires org.elasticsearch.server;
    requires org.elasticsearch.xcontent;
    requires org.elasticsearch.xcore;

    requires reactor.core;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    exports org.elasticsearch.xpack.mcp;
    exports org.elasticsearch.xpack.mcp.transport;
}
