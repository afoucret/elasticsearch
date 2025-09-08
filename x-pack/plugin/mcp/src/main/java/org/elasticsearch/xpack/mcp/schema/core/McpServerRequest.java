/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema.core;

import org.elasticsearch.xpack.mcp.schema.elicitation.McpElicitRequest;
import org.elasticsearch.xpack.mcp.schema.root.McpListRootsRequest;
import org.elasticsearch.xpack.mcp.schema.sampling.McpCreateMessageRequest;

;

public sealed interface McpServerRequest extends McpRequest permits McpPingRequest, McpCreateMessageRequest, McpListRootsRequest,
    McpElicitRequest {}
