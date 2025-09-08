/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.core;

import org.elasticsearch.xpack.mcp.schema.logging.McpLoggingMessageNotification;
import org.elasticsearch.xpack.mcp.schema.prompt.McpPromptsListChangedNotification;
import org.elasticsearch.xpack.mcp.schema.resource.McpResourcesListChangedNotification;
import org.elasticsearch.xpack.mcp.schema.resource.McpResourcesUpdatedNotification;
import org.elasticsearch.xpack.mcp.schema.tool.McpToolsListChangedNotification;

public sealed interface McpServerNotification extends McpNotification permits McpCancelledNotification, McpProgressNotification,
    McpLoggingMessageNotification, McpResourcesUpdatedNotification, McpResourcesListChangedNotification, McpToolsListChangedNotification,
    McpPromptsListChangedNotification {}
