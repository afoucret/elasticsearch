/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.core;

import org.elasticsearch.xpack.mcp.schema.completion.McpCompleteResult;
import org.elasticsearch.xpack.mcp.schema.handshake.McpInitializeResult;
import org.elasticsearch.xpack.mcp.schema.prompt.McpGetPromptResult;
import org.elasticsearch.xpack.mcp.schema.prompt.McpListPromptsResult;
import org.elasticsearch.xpack.mcp.schema.resource.McpListResourceTemplatesResult;
import org.elasticsearch.xpack.mcp.schema.resource.McpListResourcesResult;
import org.elasticsearch.xpack.mcp.schema.resource.McpReadResourceResult;
import org.elasticsearch.xpack.mcp.schema.tool.McpCallToolResult;
import org.elasticsearch.xpack.mcp.schema.tool.McpListToolsResult;

public sealed interface McpServerResult extends McpResult permits McpInitializeResult, McpCompleteResult, McpGetPromptResult,
    McpListPromptsResult, McpListResourceTemplatesResult, McpListResourcesResult, McpReadResourceResult, McpCallToolResult,
    McpListToolsResult, McpEmptyResult {}
