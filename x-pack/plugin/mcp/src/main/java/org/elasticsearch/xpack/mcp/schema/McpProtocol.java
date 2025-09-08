/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.NamedWriteableRegistry;

import java.util.List;

public class McpProtocol {

    public static List<NamedWriteableRegistry.Entry> namedWriteables() {
        return List.of(
            McpAnnotations.NAMED_WRITEABLE_ENTRY,
            new NamedWriteableRegistry.Entry(McpAudioContent.class, McpAudioContent.NAME, McpAudioContent::new),
            new NamedWriteableRegistry.Entry(McpBlobResourceContents.class, McpBlobResourceContents.NAME, McpBlobResourceContents::new),
            new NamedWriteableRegistry.Entry(McpResourceContent.class, McpTextResourceContents.NAME, McpTextResourceContents::new),
            new NamedWriteableRegistry.Entry(McpResourceContent.class, McpBlobResourceContents.NAME, McpBlobResourceContents::new),
            new NamedWriteableRegistry.Entry(McpCallToolRequest.class, McpCallToolRequest.NAME, McpCallToolRequest::new),
            new NamedWriteableRegistry.Entry(McpCallToolResult.class, McpCallToolResult.NAME, McpCallToolResult::new),
            new NamedWriteableRegistry.Entry(McpCancelledNotification.class, McpCancelledNotification.NAME, McpCancelledNotification::new),
            new NamedWriteableRegistry.Entry(McpClientCapabilities.class, McpClientCapabilities.NAME, McpClientCapabilities::new),
            new NamedWriteableRegistry.Entry(
                McpClientCapabilities.Elicitation.class,
                McpClientCapabilities.Elicitation.NAME,
                McpClientCapabilities.Elicitation::new
            ),
            new NamedWriteableRegistry.Entry(
                McpClientCapabilities.RootCapabilities.class,
                McpClientCapabilities.RootCapabilities.NAME,
                McpClientCapabilities.RootCapabilities::new
            ),
            new NamedWriteableRegistry.Entry(
                McpClientCapabilities.Sampling.class,
                McpClientCapabilities.Sampling.NAME,
                McpClientCapabilities.Sampling::new
            ),
            new NamedWriteableRegistry.Entry(McpCompleteRequest.class, McpCompleteRequest.NAME, McpCompleteRequest::new),
            new NamedWriteableRegistry.Entry(
                McpCompleteRequest.Argument.class,
                McpCompleteRequest.Argument.NAME,
                McpCompleteRequest.Argument::new
            ),
            new NamedWriteableRegistry.Entry(
                McpCompleteRequest.Context.class,
                McpCompleteRequest.Context.NAME,
                McpCompleteRequest.Context::new
            ),
            new NamedWriteableRegistry.Entry(McpCompleteResult.class, McpCompleteResult.NAME, McpCompleteResult::new),
            new NamedWriteableRegistry.Entry(
                McpCompleteResult.Completion.class,
                McpCompleteResult.Completion.NAME,
                McpCompleteResult.Completion::new
            ),
            new NamedWriteableRegistry.Entry(McpContent.class, McpTextContent.NAME, McpTextContent::new),
            new NamedWriteableRegistry.Entry(McpContent.class, McpAudioContent.NAME, McpAudioContent::new),
            new NamedWriteableRegistry.Entry(McpContent.class, McpImageContent.NAME, McpImageContent::new),
            new NamedWriteableRegistry.Entry(McpContent.class, McpResourceLink.NAME, McpResourceLink::new),
            new NamedWriteableRegistry.Entry(McpContent.class, McpEmbeddedResource.NAME, McpEmbeddedResource::new),
            new NamedWriteableRegistry.Entry(McpCreateMessageRequest.class, McpCreateMessageRequest.NAME, McpCreateMessageRequest::new),
            new NamedWriteableRegistry.Entry(McpCreateMessageResult.class, McpCreateMessageResult.NAME, McpCreateMessageResult::new),
            new NamedWriteableRegistry.Entry(McpElicitRequest.class, McpElicitRequest.NAME, McpElicitRequest::new),
            new NamedWriteableRegistry.Entry(McpElicitResult.class, McpElicitResult.NAME, McpElicitResult::new),
            new NamedWriteableRegistry.Entry(McpEmbeddedResource.class, McpEmbeddedResource.NAME, McpEmbeddedResource::new),
            new NamedWriteableRegistry.Entry(McpEmptyResult.class, McpEmptyResult.NAME, McpEmptyResult::new),
            new NamedWriteableRegistry.Entry(McpGetPromptRequest.class, McpGetPromptRequest.NAME, McpGetPromptRequest::new),
            new NamedWriteableRegistry.Entry(McpGetPromptResult.class, McpGetPromptResult.NAME, McpGetPromptResult::new),
            new NamedWriteableRegistry.Entry(McpImageContent.class, McpImageContent.NAME, McpImageContent::new),
            new NamedWriteableRegistry.Entry(McpImplementation.class, McpImplementation.NAME, McpImplementation::new),
            new NamedWriteableRegistry.Entry(
                McpInitializedNotification.class,
                McpInitializedNotification.NAME,
                McpInitializedNotification::new
            ),
            new NamedWriteableRegistry.Entry(McpInitializeRequest.class, McpInitializeRequest.NAME, McpInitializeRequest::new),
            new NamedWriteableRegistry.Entry(McpInitializeResult.class, McpInitializeResult.NAME, McpInitializeResult::new),
            new NamedWriteableRegistry.Entry(McpJsonSchema.class, McpJsonSchema.NAME, McpJsonSchema::new),
            new NamedWriteableRegistry.Entry(McpListPromptsRequest.class, McpListPromptsRequest.NAME, McpListPromptsRequest::new),
            new NamedWriteableRegistry.Entry(McpListPromptsResult.class, McpListPromptsResult.NAME, McpListPromptsResult::new),
            new NamedWriteableRegistry.Entry(McpListResourcesRequest.class, McpListResourcesRequest.NAME, McpListResourcesRequest::new),
            new NamedWriteableRegistry.Entry(McpListResourcesResult.class, McpListResourcesResult.NAME, McpListResourcesResult::new),
            new NamedWriteableRegistry.Entry(
                McpListResourceTemplatesResult.class,
                McpListResourceTemplatesResult.NAME,
                McpListResourceTemplatesResult::new
            ),
            new NamedWriteableRegistry.Entry(
                McpListResourceTemplatesRequest.class,
                McpListResourceTemplatesRequest.NAME,
                McpListResourceTemplatesRequest::new
            ),
            new NamedWriteableRegistry.Entry(McpListRootsRequest.class, McpListRootsRequest.NAME, McpListRootsRequest::new),
            new NamedWriteableRegistry.Entry(McpListRootsResult.class, McpListRootsResult.NAME, McpListRootsResult::new),
            new NamedWriteableRegistry.Entry(McpListToolsRequest.class, McpListToolsRequest.NAME, McpListToolsRequest::new),
            new NamedWriteableRegistry.Entry(McpListToolsResult.class, McpListToolsResult.NAME, McpListToolsResult::new),
            new NamedWriteableRegistry.Entry(
                McpLoggingMessageNotification.class,
                McpLoggingMessageNotification.NAME,
                McpLoggingMessageNotification::new
            ),
            new NamedWriteableRegistry.Entry(
                McpLoggingSetLevelRequest.class,
                McpLoggingSetLevelRequest.NAME,
                McpLoggingSetLevelRequest::new
            ),
            new NamedWriteableRegistry.Entry(McpModelHint.class, McpModelHint.NAME, McpModelHint::new),
            new NamedWriteableRegistry.Entry(McpModelPreferences.class, McpModelPreferences.NAME, McpModelPreferences::new),
            new NamedWriteableRegistry.Entry(McpPingRequest.class, McpPingRequest.NAME, McpPingRequest::new),
            new NamedWriteableRegistry.Entry(McpProgressNotification.class, McpProgressNotification.NAME, McpProgressNotification::new),
            new NamedWriteableRegistry.Entry(McpPrompt.class, McpPrompt.NAME, McpPrompt::new),
            new NamedWriteableRegistry.Entry(McpPromptArgument.class, McpPromptArgument.NAME, McpPromptArgument::new),
            new NamedWriteableRegistry.Entry(McpPromptMessage.class, McpPromptMessage.NAME, McpPromptMessage::new),
            new NamedWriteableRegistry.Entry(McpPromptReference.class, McpPromptReference.NAME, McpPromptReference::new),
            new NamedWriteableRegistry.Entry(
                McpPromptsListChangedNotification.class,
                McpPromptsListChangedNotification.NAME,
                McpPromptsListChangedNotification::new
            ),
            new NamedWriteableRegistry.Entry(McpReadResourceRequest.class, McpReadResourceRequest.NAME, McpReadResourceRequest::new),
            new NamedWriteableRegistry.Entry(McpReadResourceResult.class, McpReadResourceResult.NAME, McpReadResourceResult::new),
            new NamedWriteableRegistry.Entry(McpReference.class, McpReference.NAME, McpReference::read),
            new NamedWriteableRegistry.Entry(McpReference.class, McpPromptReference.NAME, McpReference::read),
            new NamedWriteableRegistry.Entry(McpReference.class, McpResourceTemplateReference.NAME, McpReference::read),
            new NamedWriteableRegistry.Entry(McpResource.class, McpResource.NAME, McpResource::new),
            new NamedWriteableRegistry.Entry(McpResourceLink.class, McpResourceLink.NAME, McpResourceLink::new),
            new NamedWriteableRegistry.Entry(
                McpResourcesListChangedNotification.class,
                McpResourcesListChangedNotification.NAME,
                McpResourcesListChangedNotification::new
            ),
            new NamedWriteableRegistry.Entry(
                McpResourcesUpdatedNotification.class,
                McpResourcesUpdatedNotification.NAME,
                McpResourcesUpdatedNotification::new
            ),
            new NamedWriteableRegistry.Entry(McpResourceTemplate.class, McpResourceTemplate.NAME, McpResourceTemplate::new),
            new NamedWriteableRegistry.Entry(
                McpResourceTemplateReference.class,
                McpResourceTemplateReference.NAME,
                McpResourceTemplateReference::new
            ),
            new NamedWriteableRegistry.Entry(McpRoot.class, McpRoot.NAME, McpRoot::new),
            new NamedWriteableRegistry.Entry(
                McpRootsListChangedNotification.class,
                McpRootsListChangedNotification.NAME,
                McpRootsListChangedNotification::new
            ),
            new NamedWriteableRegistry.Entry(McpSamplingMessage.class, McpSamplingMessage.NAME, McpSamplingMessage::new),
            new NamedWriteableRegistry.Entry(McpServerCapabilities.class, McpServerCapabilities.NAME, McpServerCapabilities::new),
            new NamedWriteableRegistry.Entry(
                McpServerCapabilities.PromptCapabilities.class,
                McpServerCapabilities.PromptCapabilities.NAME,
                McpServerCapabilities.PromptCapabilities::new
            ),
            new NamedWriteableRegistry.Entry(
                McpServerCapabilities.LoggingCapabilities.class,
                McpServerCapabilities.LoggingCapabilities.NAME,
                McpServerCapabilities.LoggingCapabilities::new
            ),
            new NamedWriteableRegistry.Entry(
                McpServerCapabilities.CompletionCapabilities.class,
                McpServerCapabilities.CompletionCapabilities.NAME,
                McpServerCapabilities.CompletionCapabilities::new
            ),
            new NamedWriteableRegistry.Entry(
                McpServerCapabilities.ToolCapabilities.class,
                McpServerCapabilities.ToolCapabilities.NAME,
                McpServerCapabilities.ToolCapabilities::new
            ),
            new NamedWriteableRegistry.Entry(
                McpServerCapabilities.ResourceCapabilities.class,
                McpServerCapabilities.ResourceCapabilities.NAME,
                McpServerCapabilities.ResourceCapabilities::new
            ),

            new NamedWriteableRegistry.Entry(McpSubscribeRequest.class, McpSubscribeRequest.NAME, McpSubscribeRequest::new),
            new NamedWriteableRegistry.Entry(McpTextContent.class, McpTextContent.NAME, McpTextContent::new),
            new NamedWriteableRegistry.Entry(McpTextResourceContents.class, McpTextResourceContents.NAME, McpTextResourceContents::new),
            new NamedWriteableRegistry.Entry(McpTool.class, McpTool.NAME, McpTool::new),
            new NamedWriteableRegistry.Entry(McpToolAnnotations.class, McpToolAnnotations.NAME, McpToolAnnotations::new),
            new NamedWriteableRegistry.Entry(
                McpToolsListChangedNotification.class,
                McpToolsListChangedNotification.NAME,
                McpToolsListChangedNotification::new
            ),
            new NamedWriteableRegistry.Entry(McpUnsubscribeRequest.class, McpUnsubscribeRequest.NAME, McpUnsubscribeRequest::new)
        );
    }
}
