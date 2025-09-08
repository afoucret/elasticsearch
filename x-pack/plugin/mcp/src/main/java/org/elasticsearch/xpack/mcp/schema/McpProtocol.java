/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.xcontent.NamedXContentRegistry;
import org.elasticsearch.xcontent.ParseField;

import java.util.List;

public class McpProtocol {

    public static List<NamedWriteableRegistry.Entry> namedWriteables() {
        return List.of(
            new NamedWriteableRegistry.Entry(McpAnnotations.class, McpAnnotations.NAME, McpAnnotations::new),
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

    public static List<NamedXContentRegistry.Entry> namedXContent() {
        return List.of(
            new NamedXContentRegistry.Entry(McpAnnotations.class, new ParseField(McpAnnotations.NAME), McpAnnotations.PARSER),
            new NamedXContentRegistry.Entry(McpAudioContent.class, new ParseField(McpAudioContent.NAME), McpAudioContent.PARSER),
            new NamedXContentRegistry.Entry(
                McpBlobResourceContents.class,
                new ParseField(McpBlobResourceContents.NAME),
                McpBlobResourceContents.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpResourceContent.class,
                new ParseField(McpResourceContent.NAME),
                McpResourceContent::fromXContent
            ),
            new NamedXContentRegistry.Entry(McpCallToolRequest.class, new ParseField(McpCallToolRequest.NAME), McpCallToolRequest.PARSER),
            new NamedXContentRegistry.Entry(McpCallToolResult.class, new ParseField(McpCallToolResult.NAME), McpCallToolResult.PARSER),
            new NamedXContentRegistry.Entry(
                McpCancelledNotification.class,
                new ParseField(McpCancelledNotification.NAME),
                McpCancelledNotification.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpClientCapabilities.class,
                new ParseField(McpClientCapabilities.NAME),
                McpClientCapabilities.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpClientCapabilities.Elicitation.class,
                new ParseField(McpClientCapabilities.Elicitation.NAME),
                McpClientCapabilities.Elicitation.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpClientCapabilities.RootCapabilities.class,
                new ParseField(McpClientCapabilities.RootCapabilities.NAME),
                McpClientCapabilities.RootCapabilities.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpClientCapabilities.Sampling.class,
                new ParseField(McpClientCapabilities.Sampling.NAME),
                McpClientCapabilities.Sampling.PARSER
            ),
            new NamedXContentRegistry.Entry(McpCompleteRequest.class, new ParseField(McpCompleteRequest.NAME), McpCompleteRequest.PARSER),
            new NamedXContentRegistry.Entry(
                McpCompleteRequest.Argument.class,
                new ParseField(McpCompleteRequest.Argument.NAME),
                McpCompleteRequest.Argument.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpCompleteRequest.Context.class,
                new ParseField(McpCompleteRequest.Context.NAME),
                McpCompleteRequest.Context.PARSER
            ),
            new NamedXContentRegistry.Entry(McpCompleteResult.class, new ParseField(McpCompleteResult.NAME), McpCompleteResult.PARSER),
            new NamedXContentRegistry.Entry(
                McpCompleteResult.Completion.class,
                new ParseField(McpCompleteResult.Completion.NAME),
                McpCompleteResult.Completion.PARSER
            ),

            new NamedXContentRegistry.Entry(McpContent.class, new ParseField(McpContent.NAME), McpContent::fromXContent),
            new NamedXContentRegistry.Entry(McpContent.class, new ParseField(McpTextContent.NAME), McpContent::fromXContent),
            new NamedXContentRegistry.Entry(McpContent.class, new ParseField(McpAudioContent.NAME), McpContent::fromXContent),
            new NamedXContentRegistry.Entry(McpContent.class, new ParseField(McpImageContent.NAME), McpContent::fromXContent),
            new NamedXContentRegistry.Entry(McpContent.class, new ParseField(McpResourceLink.NAME), McpContent::fromXContent),
            new NamedXContentRegistry.Entry(McpContent.class, new ParseField(McpEmbeddedResource.NAME), McpContent::fromXContent),

            new NamedXContentRegistry.Entry(
                McpCreateMessageRequest.class,
                new ParseField(McpCreateMessageRequest.NAME),
                McpCreateMessageRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpCreateMessageResult.class,
                new ParseField(McpCreateMessageResult.NAME),
                McpCreateMessageResult.PARSER
            ),
            new NamedXContentRegistry.Entry(McpElicitRequest.class, new ParseField(McpElicitRequest.NAME), McpElicitRequest.PARSER),
            new NamedXContentRegistry.Entry(McpElicitResult.class, new ParseField(McpElicitResult.NAME), McpElicitResult.PARSER),
            new NamedXContentRegistry.Entry(McpEmptyResult.class, new ParseField(McpEmptyResult.NAME), McpEmptyResult.PARSER),
            new NamedXContentRegistry.Entry(
                McpGetPromptRequest.class,
                new ParseField(McpGetPromptRequest.NAME),
                McpGetPromptRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(McpGetPromptResult.class, new ParseField(McpGetPromptResult.NAME), McpGetPromptResult.PARSER),
            new NamedXContentRegistry.Entry(McpImageContent.class, new ParseField(McpImageContent.NAME), McpImageContent.PARSER),
            new NamedXContentRegistry.Entry(McpImplementation.class, new ParseField(McpImplementation.NAME), McpImplementation.PARSER),
            new NamedXContentRegistry.Entry(
                McpInitializedNotification.class,
                new ParseField(McpInitializedNotification.NAME),
                McpInitializedNotification.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpInitializeRequest.class,
                new ParseField(McpInitializeRequest.NAME),
                McpInitializeRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpInitializeResult.class,
                new ParseField(McpInitializeResult.NAME),
                McpInitializeResult.PARSER
            ),
            new NamedXContentRegistry.Entry(McpJsonSchema.class, new ParseField(McpJsonSchema.NAME), McpJsonSchema.PARSER),
            new NamedXContentRegistry.Entry(
                McpListPromptsRequest.class,
                new ParseField(McpListPromptsRequest.NAME),
                McpListPromptsRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpListPromptsResult.class,
                new ParseField(McpListPromptsResult.NAME),
                McpListPromptsResult.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpListResourcesRequest.class,
                new ParseField(McpListResourcesRequest.NAME),
                McpListResourcesRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpListResourcesResult.class,
                new ParseField(McpListResourcesResult.NAME),
                McpListResourcesResult.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpListResourceTemplatesResult.class,
                new ParseField(McpListResourceTemplatesResult.NAME),
                McpListResourceTemplatesResult.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpListResourceTemplatesRequest.class,
                new ParseField(McpListResourceTemplatesRequest.NAME),
                McpListResourceTemplatesRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpListRootsRequest.class,
                new ParseField(McpListRootsRequest.NAME),
                McpListRootsRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(McpListRootsResult.class, new ParseField(McpListRootsResult.NAME), McpListRootsResult.PARSER),
            new NamedXContentRegistry.Entry(
                McpListToolsRequest.class,
                new ParseField(McpListToolsRequest.NAME),
                McpListToolsRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(McpListToolsResult.class, new ParseField(McpListToolsResult.NAME), McpListToolsResult.PARSER),
            new NamedXContentRegistry.Entry(
                McpLoggingMessageNotification.class,
                new ParseField(McpLoggingMessageNotification.NAME),
                McpLoggingMessageNotification.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpLoggingSetLevelRequest.class,
                new ParseField(McpLoggingSetLevelRequest.NAME),
                McpLoggingSetLevelRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(McpModelHint.class, new ParseField(McpModelHint.NAME), McpModelHint.PARSER),
            new NamedXContentRegistry.Entry(
                McpModelPreferences.class,
                new ParseField(McpModelPreferences.NAME),
                McpModelPreferences.PARSER
            ),
            new NamedXContentRegistry.Entry(McpPingRequest.class, new ParseField(McpPingRequest.NAME), McpPingRequest.PARSER),
            new NamedXContentRegistry.Entry(
                McpProgressNotification.class,
                new ParseField(McpProgressNotification.NAME),
                McpProgressNotification.PARSER
            ),
            new NamedXContentRegistry.Entry(McpPrompt.class, new ParseField(McpPrompt.NAME), McpPrompt.PARSER),
            new NamedXContentRegistry.Entry(McpPromptArgument.class, new ParseField(McpPromptArgument.NAME), McpPromptArgument.PARSER),
            new NamedXContentRegistry.Entry(McpPromptMessage.class, new ParseField(McpPromptMessage.NAME), McpPromptMessage.PARSER),
            new NamedXContentRegistry.Entry(McpPromptReference.class, new ParseField(McpPromptReference.NAME), McpPromptReference.PARSER),
            new NamedXContentRegistry.Entry(
                McpPromptsListChangedNotification.class,
                new ParseField(McpPromptsListChangedNotification.NAME),
                McpPromptsListChangedNotification.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpReadResourceRequest.class,
                new ParseField(McpReadResourceRequest.NAME),
                McpReadResourceRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpReadResourceResult.class,
                new ParseField(McpReadResourceResult.NAME),
                McpReadResourceResult.PARSER
            ),
            new NamedXContentRegistry.Entry(McpReference.class, new ParseField(McpReference.NAME), McpReference::fromXContent),
            new NamedXContentRegistry.Entry(McpResource.class, new ParseField(McpResource.NAME), McpResource.PARSER),
            new NamedXContentRegistry.Entry(McpResourceLink.class, new ParseField(McpResourceLink.NAME), McpResourceLink.PARSER),
            new NamedXContentRegistry.Entry(
                McpResourcesListChangedNotification.class,
                new ParseField(McpResourcesListChangedNotification.NAME),
                McpResourcesListChangedNotification.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpResourcesUpdatedNotification.class,
                new ParseField(McpResourcesUpdatedNotification.NAME),
                McpResourcesUpdatedNotification.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpResourceTemplate.class,
                new ParseField(McpResourceTemplate.NAME),
                McpResourceTemplate.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpResourceTemplateReference.class,
                new ParseField(McpResourceTemplateReference.NAME),
                McpResourceTemplateReference.PARSER
            ),
            new NamedXContentRegistry.Entry(McpRoot.class, new ParseField(McpRoot.NAME), McpRoot.PARSER),
            new NamedXContentRegistry.Entry(
                McpRootsListChangedNotification.class,
                new ParseField(McpRootsListChangedNotification.NAME),
                McpRootsListChangedNotification.PARSER
            ),
            new NamedXContentRegistry.Entry(McpSamplingMessage.class, new ParseField(McpSamplingMessage.NAME), McpSamplingMessage.PARSER),
            new NamedXContentRegistry.Entry(
                McpServerCapabilities.class,
                new ParseField(McpServerCapabilities.NAME),
                McpServerCapabilities.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpSubscribeRequest.class,
                new ParseField(McpSubscribeRequest.NAME),
                McpSubscribeRequest.PARSER
            ),
            new NamedXContentRegistry.Entry(McpTextContent.class, new ParseField(McpTextContent.NAME), McpTextContent.PARSER),
            new NamedXContentRegistry.Entry(
                McpTextResourceContents.class,
                new ParseField(McpTextResourceContents.NAME),
                McpTextResourceContents.PARSER
            ),
            new NamedXContentRegistry.Entry(McpTool.class, new ParseField(McpTool.NAME), McpTool.PARSER),
            new NamedXContentRegistry.Entry(McpToolAnnotations.class, new ParseField(McpToolAnnotations.NAME), McpToolAnnotations.PARSER),
            new NamedXContentRegistry.Entry(
                McpToolsListChangedNotification.class,
                new ParseField(McpToolsListChangedNotification.NAME),
                McpToolsListChangedNotification.PARSER
            ),
            new NamedXContentRegistry.Entry(
                McpUnsubscribeRequest.class,
                new ParseField(McpUnsubscribeRequest.NAME),
                McpUnsubscribeRequest.PARSER
            )
        );
    }
}
