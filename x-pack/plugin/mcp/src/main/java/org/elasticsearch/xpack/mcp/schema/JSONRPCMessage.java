/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.xcontent.ToXContentObject;

/**
 * A sealed interface for all JSON-RPC messages, ensuring type safety.
 * It permits only the specific request, response, and notification types.
 */
public sealed interface JSONRPCMessage extends NamedWriteable, ToXContentObject permits JSONRPCRequest, JSONRPCResponse,
    JSONRPCNotification {
    String jsonrpc();
}
