/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.xpack.mcp.schema.capability.McpClientCapabilities;
import org.elasticsearch.xpack.mcp.schema.capability.McpServerCapabilities;
import org.elasticsearch.xpack.mcp.schema.completion.McpCompleteRequest;
import org.elasticsearch.xpack.mcp.schema.completion.McpCompleteResult;
import org.elasticsearch.xpack.mcp.schema.content.McpAudioContent;
import org.elasticsearch.xpack.mcp.schema.content.McpEmbeddedResource;
import org.elasticsearch.xpack.mcp.schema.content.McpImageContent;
import org.elasticsearch.xpack.mcp.schema.content.McpResourceLink;
import org.elasticsearch.xpack.mcp.schema.content.McpTextContent;
import org.elasticsearch.xpack.mcp.schema.core.McpAnnotations;
import org.elasticsearch.xpack.mcp.schema.core.McpEmptyResult;
import org.elasticsearch.xpack.mcp.schema.core.McpJsonSchema;
import org.elasticsearch.xpack.mcp.schema.core.McpPingRequest;
import org.elasticsearch.xpack.mcp.schema.core.McpProgressNotification;
import org.elasticsearch.xpack.mcp.schema.elicitation.McpElicitRequest;
import org.elasticsearch.xpack.mcp.schema.elicitation.McpElicitResult;
import org.elasticsearch.xpack.mcp.schema.handshake.McpImplementation;
import org.elasticsearch.xpack.mcp.schema.handshake.McpInitializedNotification;
import org.elasticsearch.xpack.mcp.schema.handshake.McpInitializeRequest;
import org.elasticsearch.xpack.mcp.schema.handshake.McpInitializeResult;
import org.elasticsearch.xpack.mcp.schema.logging.McpLoggingMessageNotification;
import org.elasticsearch.xpack.mcp.schema.logging.McpLoggingSetLevelRequest;
import org.elasticsearch.xpack.mcp.schema.messaging.McpCreateMessageRequest;
import org.elasticsearch.xpack.mcp.schema.messaging.McpCreateMessageResult;
import org.elasticsearch.xpack.mcp.schema.messaging.McpSamplingMessage;
import org.elasticsearch.xpack.mcp.schema.model.McpModelHint;
import org.elasticsearch.xpack.mcp.schema.model.McpModelPreferences;
import org.elasticsearch.xpack.mcp.schema.prompt.McpGetPromptRequest;
import org.elasticsearch.xpack.mcp.schema.prompt.McpGetPromptResult;
import org.elasticsearch.xpack.mcp.schema.prompt.McpListPromptsRequest;
import org.elasticsearch.xpack.mcp.schema.prompt.McpListPromptsResult;
import org.elasticsearch.xpack.mcp.schema.prompt.McpPrompt;
import org.elasticsearch.xpack.mcp.schema.prompt.McpPromptArgument;
import org.elasticsearch.xpack.mcp.schema.prompt.McpPromptMessage;
import org.elasticsearch.xpack.mcp.schema.prompt.McpPromptReference;
import org.elasticsearch.xpack.mcp.schema.prompt.McpPromptsListChangedNotification;
import org.elasticsearch.xpack.mcp.schema.resource.McpBlobResourceContents;
import org.elasticsearch.xpack.mcp.schema.resource.McpListResourcesRequest;
import org.elasticsearch.xpack.mcp.schema.resource.McpListResourcesResult;
import org.elasticsearch.xpack.mcp.schema.resource.McpListResourceTemplatesRequest;
import org.elasticsearch.xpack.mcp.schema.resource.McpListResourceTemplatesResult;
import org.elasticsearch.xpack.mcp.schema.resource.McpReadResourceRequest;
import org.elasticsearch.xpack.mcp.schema.resource.McpReadResourceResult;
import org.elasticsearch.xpack.mcp.schema.resource.McpResource;
import org.elasticsearch.xpack.mcp.schema.resource.McpResourcesListChangedNotification;
import org.elasticsearch.xpack.mcp.schema.resource.McpResourcesUpdatedNotification;
import org.elasticsearch.xpack.mcp.schema.resource.McpResourceTemplate;
import org.elasticsearch.xpack.mcp.schema.resource.McpResourceTemplateReference;
import org.elasticsearch.xpack.mcp.schema.resource.McpSubscribeRequest;
import org.elasticsearch.xpack.mcp.schema.resource.McpTextResourceContents;
import org.elasticsearch.xpack.mcp.schema.resource.McpUnsubscribeRequest;
import org.elasticsearch.xpack.mcp.schema.root.McpListRootsRequest;
import org.elasticsearch.xpack.mcp.schema.root.McpListRootsResult;
import org.elasticsearch.xpack.mcp.schema.root.McpRoot;
import org.elasticsearch.xpack.mcp.schema.root.McpRootsListChangedNotification;
import org.elasticsearch.xpack.mcp.schema.tool.McpCallToolRequest;
import org.elasticsearch.xpack.mcp.schema.tool.McpCallToolResult;
import org.elasticsearch.xpack.mcp.schema.tool.McpListToolsRequest;
import org.elasticsearch.xpack.mcp.schema.tool.McpListToolsResult;
import org.elasticsearch.xpack.mcp.schema.tool.McpTool;
import org.elasticsearch.xpack.mcp.schema.tool.McpToolAnnotations;
import org.elasticsearch.xpack.mcp.schema.tool.McpToolsListChangedNotification;

import java.util.List;

public class McpProtocol {

    public static List<NamedWriteableRegistry.Entry> namedWriteables() {
        return List.of(
            McpAnnotations.NAMED_WRITEABLE_ENTRY,
            McpAudioContent.NAMED_WRITEABLE_ENTRY,
            McpBlobResourceContents.NAMED_WRITEABLE_ENTRY,
            McpTextResourceContents.NAMED_WRITEABLE_ENTRY,
            McpImageContent.NAMED_WRITEABLE_ENTRY,
            McpTextContent.NAMED_WRITEABLE_ENTRY,
            McpCallToolRequest.NAMED_WRITEABLE_ENTRY,
            McpCallToolResult.NAMED_WRITEABLE_ENTRY,
            McpCancelledNotification.NAMED_WRITEABLE_ENTRY,
            McpClientCapabilities.NAMED_WRITEABLE_ENTRY,
            McpClientCapabilities.Elicitation.NAMED_WRITEABLE_ENTRY,
            McpClientCapabilities.RootCapabilities.NAMED_WRITEABLE_ENTRY,
            McpClientCapabilities.Sampling.NAMED_WRITEABLE_ENTRY,
            McpCompleteRequest.NAMED_WRITEABLE_ENTRY,
            McpCompleteRequest.Argument.NAMED_WRITEABLE_ENTRY,
            McpCompleteRequest.Context.NAMED_WRITEABLE_ENTRY,
            McpCompleteResult.NAMED_WRITEABLE_ENTRY,
            McpCompleteResult.Completion.NAMED_WRITEABLE_ENTRY,
            McpCreateMessageRequest.NAMED_WRITEABLE_ENTRY,
            McpCreateMessageResult.NAMED_WRITEABLE_ENTRY,
            McpElicitRequest.NAMED_WRITEABLE_ENTRY,
            McpElicitResult.NAMED_WRITEABLE_ENTRY,
            McpEmbeddedResource.NAMED_WRITEABLE_ENTRY,
            McpEmptyResult.NAMED_WRITEABLE_ENTRY,
            McpGetPromptRequest.NAMED_WRITEABLE_ENTRY,
            McpGetPromptResult.NAMED_WRITEABLE_ENTRY,
            McpImplementation.NAMED_WRITEABLE_ENTRY,
            McpInitializedNotification.NAMED_WRITEABLE_ENTRY,
            McpInitializeRequest.NAMED_WRITEABLE_ENTRY,
            McpInitializeResult.NAMED_WRITEABLE_ENTRY,
            McpJsonSchema.NAMED_WRITEABLE_ENTRY,
            McpListPromptsRequest.NAMED_WRITEABLE_ENTRY,
            McpListPromptsResult.NAMED_WRITEABLE_ENTRY,
            McpListResourcesRequest.NAMED_WRITEABLE_ENTRY,
            McpListResourcesResult.NAMED_WRITEABLE_ENTRY,
            McpListResourceTemplatesRequest.NAMED_WRITEABLE_ENTRY,
            McpListResourceTemplatesResult.NAMED_WRITEABLE_ENTRY,
            McpListRootsRequest.NAMED_WRITEABLE_ENTRY,
            McpListRootsResult.NAMED_WRITEABLE_ENTRY,
            McpListToolsRequest.NAMED_WRITEABLE_ENTRY,
            McpListToolsResult.NAMED_WRITEABLE_ENTRY,
            McpLoggingMessageNotification.NAMED_WRITEABLE_ENTRY,
            McpLoggingSetLevelRequest.NAMED_WRITEABLE_ENTRY,
            McpModelHint.NAMED_WRITEABLE_ENTRY,
            McpModelPreferences.NAMED_WRITEABLE_ENTRY,
            McpPingRequest.NAMED_WRITEABLE_ENTRY,
            McpProgressNotification.NAMED_WRITEABLE_ENTRY,
            McpPrompt.NAMED_WRITEABLE_ENTRY,
            McpPromptArgument.NAMED_WRITEABLE_ENTRY,
            McpPromptMessage.NAMED_WRITEABLE_ENTRY,
            McpPromptReference.NAMED_WRITEABLE_ENTRY,
            McpPromptsListChangedNotification.NAMED_WRITEABLE_ENTRY,
            McpReadResourceRequest.NAMED_WRITEABLE_ENTRY,
            McpReadResourceResult.NAMED_WRITEABLE_ENTRY,
            McpResource.NAMED_WRITEABLE_ENTRY,
            McpResourceLink.NAMED_WRITEABLE_ENTRY,
            McpResourcesListChangedNotification.NAMED_WRITEABLE_ENTRY,
            McpResourcesUpdatedNotification.NAMED_WRITEABLE_ENTRY,
            McpResourceTemplate.NAMED_WRITEABLE_ENTRY,
            McpResourceTemplateReference.NAMED_WRITEABLE_ENTRY,
            McpRoot.NAMED_WRITEABLE_ENTRY,
            McpRootsListChangedNotification.NAMED_WRITEABLE_ENTRY,
            McpSamplingMessage.NAMED_WRITEABLE_ENTRY,
            McpServerCapabilities.NAMED_WRITEABLE_ENTRY,
            McpServerCapabilities.CompletionCapabilities.NAMED_WRITEABLE_ENTRY,
            McpServerCapabilities.LoggingCapabilities.NAMED_WRITEABLE_ENTRY,
            McpServerCapabilities.PromptCapabilities.NAMED_WRITEABLE_ENTRY,
            McpServerCapabilities.ResourceCapabilities.NAMED_WRITEABLE_ENTRY,
            McpServerCapabilities.ToolCapabilities.NAMED_WRITEABLE_ENTRY,
            McpSubscribeRequest.NAMED_WRITEABLE_ENTRY,
            McpTool.NAMED_WRITEABLE_ENTRY,
            McpToolAnnotations.NAMED_WRITEABLE_ENTRY,
            McpToolsListChangedNotification.NAMED_WRITEABLE_ENTRY,
            McpUnsubscribeRequest.NAMED_WRITEABLE_ENTRY
        );
    }
}
