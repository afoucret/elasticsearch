/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.core;

import org.elasticsearch.xpack.mcp.schema.completion.McpCompleteRequest;
import org.elasticsearch.xpack.mcp.schema.handshake.McpInitializeRequest;
import org.elasticsearch.xpack.mcp.schema.logging.McpLoggingSetLevelRequest;
import org.elasticsearch.xpack.mcp.schema.prompt.McpGetPromptRequest;
import org.elasticsearch.xpack.mcp.schema.prompt.McpListPromptsRequest;
import org.elasticsearch.xpack.mcp.schema.resource.McpListResourceTemplatesRequest;
import org.elasticsearch.xpack.mcp.schema.resource.McpListResourcesRequest;
import org.elasticsearch.xpack.mcp.schema.resource.McpReadResourceRequest;
import org.elasticsearch.xpack.mcp.schema.resource.McpSubscribeRequest;
import org.elasticsearch.xpack.mcp.schema.resource.McpUnsubscribeRequest;
import org.elasticsearch.xpack.mcp.schema.tool.McpCallToolRequest;
import org.elasticsearch.xpack.mcp.schema.tool.McpListToolsRequest;

public sealed interface McpClientRequest extends McpRequest permits McpPingRequest, McpInitializeRequest, McpCompleteRequest,
    McpGetPromptRequest, McpListPromptsRequest, McpListResourcesRequest, McpListResourceTemplatesRequest, McpReadResourceRequest,
    McpSubscribeRequest, McpUnsubscribeRequest, McpCallToolRequest, McpListToolsRequest, McpLoggingSetLevelRequest {}
