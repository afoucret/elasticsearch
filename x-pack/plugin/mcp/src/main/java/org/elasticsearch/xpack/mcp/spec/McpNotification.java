/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.spec;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.xcontent.ToXContentObject;

import java.util.Map;

public sealed interface McpNotification extends NamedWriteable, ToXContentObject permits McpInitializedNotification,
    McpProgressNotification, McpLoggingMessageNotification, McpResourcesUpdatedNotification, McpToolsListChangedNotification,
    McpResourcesListChangedNotification, McpPromptsListChangedNotification, McpRootsListChangedNotification {
    Map<String, Object> meta();
}
