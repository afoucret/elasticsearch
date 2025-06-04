/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.aiflow.mcp;

import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpClientTransport;

public class McpClientFactory {
    public McpAsyncClient getClient() {
        ServerParameters params = ServerParameters.builder("python")
            .args("-m", "mcp_server_fetch")
            .build();

        McpClientTransport transport = new StdioClientTransport(params);

        return McpClient.async(transport).build();
    }
}
