/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.core;

import org.elasticsearch.xpack.mcp.schema.handshake.McpInitializedNotification;
import org.elasticsearch.xpack.mcp.schema.root.McpRootsListChangedNotification;

public sealed interface McpClientNotification extends McpNotification permits McpCancelledNotification, McpProgressNotification,
    McpInitializedNotification, McpRootsListChangedNotification {}
