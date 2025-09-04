/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

module org.elasticsearch.xpack.mcp {
    requires org.elasticsearch.base;
    requires org.elasticsearch.server;
    requires org.elasticsearch.xcontent;
    requires org.elasticsearch.xcore;

    requires io.modelcontextprotocol.sdk.mcp;
    requires unboundid.ldapsdk;
    requires org.elasticsearch.logging;

    exports org.elasticsearch.xpack.mcp;
    exports org.elasticsearch.xpack.mcp.rest;
    exports org.elasticsearch.xpack.mcp.spec;
}
