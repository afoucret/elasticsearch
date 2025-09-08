/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.test.AbstractXContentSerializingTestCase;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.xcontent.NamedXContentRegistry;
import org.elasticsearch.xcontent.ToXContentObject;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class McpSerializationTestCase<T extends NamedWriteable & ToXContentObject> extends AbstractXContentSerializingTestCase<T> {

    public McpRoot randomRoot() {
        return new McpRoot(randomAlphaOfLength(10), randomAlphaOfLengthOrNull(10), mayBeNull(this::randomGenericMap));
    }

    public Map<String, Object> randomGenericMap() {
        return IntStream.range(0, randomIntBetween(0, 5))
            .boxed()
            .collect(Collectors.toMap(i -> randomAlphaOfLength(5), i -> randomAlphaOfLength(10)));
    }

    public McpAnnotations randomAnnotations() {
        return new McpAnnotations(mayBeNull(() -> randomList(10, () -> randomFrom(McpRole.values()))), randomOptionalDouble());
    }

    public McpResourceLink randomResourceLink() {
        return new McpResourceLink(randomAlphaOfLength(10));
    }

    public McpCreateMessageResult randomCreateMessageResult() {
        return new McpCreateMessageResult(
            randomFrom(McpRole.values()),
            randomContent(),
            randomAlphaOfLength(10),
            randomFrom(McpStopReason.values()),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpCreateMessageRequest randomCreateMessageRequest() {
        return new McpCreateMessageRequest(
            randomList(10, this::randomSamplingMessage),
            randomInt(),
            mayBeNull(this::randomModelPreferences),
            randomAlphaOfLengthOrNull(10),
            randomFrom(McpIncludeContext.values()),
            randomOptionalDouble(),
            mayBeNull(() -> randomList(10, () -> randomAlphaOfLength(10))),
            mayBeNull(this::randomGenericMap),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpElicitRequest randomElicitRequest() {
        return new McpElicitRequest(randomAlphaOfLength(10), mayBeNull(this::randomJsonSchema), mayBeNull(this::randomGenericMap));
    }

    public McpReadResourceResult randomReadResourceResult() {
        return new McpReadResourceResult(
            IntStream.range(0, randomIntBetween(0, 5)).mapToObj(i -> randomResourceContent()).collect(Collectors.toList()),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpResourceContent randomResourceContent() {
        return randomFrom(randomBlobResourceContents(), randomTextResourceContents());
    }

    private McpTextResourceContents randomTextResourceContents() {
        return new McpTextResourceContents(
            randomAlphaOfLength(10),
            randomAlphaOfLength(10),
            randomAlphaOfLength(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpReadResourceRequest randomReadResourceRequest() {
        return new McpReadResourceRequest(randomAlphaOfLength(10), mayBeNull(this::randomGenericMap));
    }

    public McpPromptsListChangedNotification randomPromptsListChangedNotification() {
        return new McpPromptsListChangedNotification(mayBeNull(this::randomGenericMap));
    }

    public McpProgressNotification randomProgressNotification() {
        return new McpProgressNotification(
            randomFrom(randomAlphaOfLength(10), randomInt()),
            randomDouble(),
            randomOptionalDouble(),
            randomAlphaOfLengthOrNull(20),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpPingRequest randomPingRequest() {
        return new McpPingRequest(mayBeNull(this::randomGenericMap));
    }

    public McpLoggingSetLevelRequest randomLoggingSetLevelRequest() {
        return new McpLoggingSetLevelRequest(randomAlphaOfLength(10), mayBeNull(this::randomGenericMap));
    }

    public McpLoggingMessageNotification randomLoggingMessageNotification() {
        return new McpLoggingMessageNotification(
            randomFrom(McpLoggingLevel.values()),
            randomGenericMap(),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpListToolsResult randomListToolsResult() {
        return new McpListToolsResult(
            IntStream.range(0, randomIntBetween(0, 5)).mapToObj(i -> randomTool()).collect(Collectors.toList()),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpResourceTemplate randomResourceTemplate() {
        return new McpResourceTemplate(
            randomAlphaOfLength(10),
            randomAlphaOfLength(10),
            randomAlphaOfLengthOrNull(10),
            randomAlphaOfLengthOrNull(10),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomAnnotations),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpListRootsResult randomListRootsResult() {
        return new McpListRootsResult(randomList(5, this::randomRoot), randomAlphaOfLengthOrNull(10), mayBeNull(this::randomGenericMap));
    }

    public McpListResourceTemplatesResult randomListResourceTemplatesResult() {
        return new McpListResourceTemplatesResult(
            randomList(5, this::randomResourceTemplate),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpListResourcesResult randomListResourcesResult() {
        return new McpListResourcesResult(
            randomList(5, this::randomResource),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpListPromptsResult randomListPromptsResult() {
        return new McpListPromptsResult(
            randomList(5, this::randomPrompt),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpInitializeResult randomInitializeResult() {
        return new McpInitializeResult(
            randomAlphaOfLength(10),
            mayBeNull(this::randomServerCapabilities),
            mayBeNull(this::randomImplementation),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpServerCapabilities randomServerCapabilities() {
        return new McpServerCapabilities(
            mayBeNull(McpServerCapabilities.CompletionCapabilities::new),
            mayBeNull(this::randomGenericMap),
            mayBeNull(McpServerCapabilities.LoggingCapabilities::new),
            mayBeNull(() -> new McpServerCapabilities.PromptCapabilities(randomOptionalBoolean())),
            mayBeNull(() -> new McpServerCapabilities.ResourceCapabilities(randomOptionalBoolean(), randomOptionalBoolean())),
            mayBeNull(() -> new McpServerCapabilities.ToolCapabilities(randomOptionalBoolean()))
        );
    }

    public McpInitializeRequest randomInitializeRequest() {
        return new McpInitializeRequest(
            randomAlphaOfLength(10),
            randomClientCapabilities(),
            randomImplementation(),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpInitializedNotification randomInitializedNotification() {
        return new McpInitializedNotification(mayBeNull(this::randomGenericMap));
    }

    public McpImplementation randomImplementation() {
        return new McpImplementation(randomAlphaOfLength(10), randomAlphaOfLength(10), randomAlphaOfLengthOrNull(10));
    }

    public McpImageContent randomImageContent() {
        return new McpImageContent(
            randomAlphaOfLength(10),
            randomAlphaOfLength(10),
            mayBeNull(this::randomAnnotations),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpGetPromptResult randomGetPromptResult() {
        return new McpGetPromptResult(
            randomList(5, this::randomPromptMessage),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpGetPromptRequest randomGetPromptRequest() {
        return new McpGetPromptRequest(randomAlphaOfLength(10), mayBeNull(this::randomGenericMap), mayBeNull(this::randomGenericMap));
    }

    public McpEmptyResult randomEmptyResult() {
        return new McpEmptyResult(mayBeNull(this::randomGenericMap));
    }

    public McpElicitResult randomElicitResult() {
        return new McpElicitResult(
            randomFrom(McpElicitResultAction.values()),
            mayBeNull(this::randomGenericMap),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpCompleteResult randomCompleteResult() {
        return new McpCompleteResult(randomCompletion(), mayBeNull(this::randomGenericMap));
    }

    public McpCompleteRequest randomCompleteRequest() {
        return new McpCompleteRequest(
            randomReference(),
            randomCompleteRequestArgument(),
            mayBeNull(this::randomCompleteRequestContext),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpReference randomReference() {
        return randomFrom(randomPromptReference(), randomResourceTemplateReference());
    }

    public McpPromptReference randomPromptReference() {
        return new McpPromptReference(randomAlphaOfLength(10), randomAlphaOfLengthOrNull(10));
    }

    public McpResourceTemplateReference randomResourceTemplateReference() {
        return new McpResourceTemplateReference(randomAlphaOfLength(10));
    }

    public McpCompleteRequest.Argument randomCompleteRequestArgument() {
        return new McpCompleteRequest.Argument(randomAlphaOfLength(10), randomAlphaOfLength(10));
    }

    public McpCompleteRequest.Context randomCompleteRequestContext() {
        return new McpCompleteRequest.Context(mayBeNull(this::randomGenericMap));
    }

    public McpCompleteResult.Completion randomCompletion() {
        return new McpCompleteResult.Completion(
            randomList(10, () -> randomAlphanumericOfLength(10)),
            randomIntOrNull(),
            randomOptionalBoolean()
        );
    }

    public McpClientCapabilities randomClientCapabilities() {
        return new McpClientCapabilities(
            mayBeNull(this::randomGenericMap),
            mayBeNull(() -> new McpClientCapabilities.RootCapabilities(randomOptionalBoolean())),
            mayBeNull(McpClientCapabilities.Sampling::new),
            mayBeNull(McpClientCapabilities.Elicitation::new)
        );
    }

    public McpCancelledNotification randomCancelledNotification() {
        return new McpCancelledNotification(
            randomFrom(randomAlphaOfLength(10), randomInt()),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpCallToolResult randomCallToolResult() {
        return new McpCallToolResult(
            randomList(10, this::randomContent),
            randomOptionalBoolean(),
            mayBeNull(this::randomGenericMap),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpCallToolRequest randomCallToolRequest() {
        return new McpCallToolRequest(randomAlphaOfLength(10), mayBeNull(this::randomGenericMap), mayBeNull(this::randomGenericMap));
    }

    public McpBlobResourceContents randomBlobResourceContents() {
        return new McpBlobResourceContents(
            randomAlphaOfLength(10),
            randomAlphaOfLength(10),
            randomAlphaOfLength(10),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpAudioContent randomAudioContent() {
        return new McpAudioContent(
            randomAlphaOfLength(10),
            randomAlphaOfLength(10),
            mayBeNull(this::randomAnnotations),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpContent randomContent() {
        return randomFrom(randomTextContent(), randomImageContent(), randomAudioContent(), randomResourceLink(), randomEmbeddedResource());
    }

    public McpEmbeddedResource randomEmbeddedResource() {
        return new McpEmbeddedResource(randomResourceContent(), mayBeNull(this::randomAnnotations), mayBeNull(this::randomGenericMap));
    }

    public McpResource randomResource() {
        return new McpResource(
            randomAlphaOfLength(10),
            randomAlphaOfLength(10),
            randomAlphaOfLengthOrNull(10),
            randomAlphaOfLengthOrNull(10),
            randomAlphaOfLengthOrNull(10),
            randomLongOrNull(),
            mayBeNull(this::randomAnnotations),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpPromptMessage randomPromptMessage() {
        return new McpPromptMessage(randomFrom(McpRole.values()), randomContent());
    }

    public McpPromptArgument randomPromptArgument() {
        return new McpPromptArgument(
            randomAlphaOfLength(10),
            randomAlphaOfLengthOrNull(10),
            randomAlphaOfLengthOrNull(10),
            randomOptionalBoolean()
        );
    }

    public McpModelPreferences randomModelPreferences() {
        return new McpModelPreferences(
            mayBeNull(() -> randomList(5, this::randomModelHint)),
            mayBeNull(ESTestCase::randomDouble),
            mayBeNull(ESTestCase::randomDouble),
            mayBeNull(ESTestCase::randomDouble)
        );
    }

    public McpPrompt randomPrompt() {
        return new McpPrompt(
            randomAlphaOfLength(10),
            randomAlphaOfLengthOrNull(10),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(() -> randomList(10, this::randomPromptArgument)),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpTool randomTool() {
        return new McpTool(
            randomAlphaOfLength(10),
            randomAlphaOfLength(10),
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomJsonSchema),
            mayBeNull(this::randomGenericMap),
            mayBeNull(this::randomToolAnnotations),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpToolAnnotations randomToolAnnotations() {
        return new McpToolAnnotations(
            randomAlphaOfLengthOrNull(10),
            randomOptionalBoolean(),
            randomOptionalBoolean(),
            randomOptionalBoolean(),
            randomOptionalBoolean(),
            randomOptionalBoolean()
        );
    }

    public McpJsonSchema randomJsonSchema() {
        return new McpJsonSchema(
            randomAlphaOfLengthOrNull(10),
            mayBeNull(this::randomGenericMap),
            mayBeNull(() -> randomList(10, () -> randomAlphaOfLength(10))),
            randomOptionalBoolean(),
            mayBeNull(this::randomGenericMap),
            mayBeNull(this::randomGenericMap)
        );
    }

    public McpListResourceTemplatesRequest randomListResourceTemplatesRequest() {
        return new McpListResourceTemplatesRequest(randomAlphaOfLengthOrNull(10), mayBeNull(this::randomGenericMap));
    }

    public McpListToolsRequest randomListToolsRequest() {
        return new McpListToolsRequest(randomAlphaOfLengthOrNull(10), mayBeNull(this::randomGenericMap));
    }

    public McpListRootsRequest randomListRootsRequest() {
        return new McpListRootsRequest(mayBeNull(this::randomGenericMap));
    }

    public McpListResourcesRequest randomListResourcesRequest() {
        return new McpListResourcesRequest(randomAlphaOfLengthOrNull(10), mayBeNull(this::randomGenericMap));
    }

    public McpListPromptsRequest randomListPromptsRequest() {
        return new McpListPromptsRequest(randomAlphaOfLengthOrNull(10), mayBeNull(this::randomGenericMap));
    }

    public McpModelHint randomModelHint() {
        return new McpModelHint(randomAlphaOfLength(10));
    }

    public McpTextContent randomTextContent() {
        return new McpTextContent(randomAlphaOfLength(10), mayBeNull(this::randomAnnotations), mayBeNull(this::randomGenericMap));
    }

    public McpUnsubscribeRequest randomUnsubscribeRequest() {
        return new McpUnsubscribeRequest(randomAlphaOfLength(10), mayBeNull(this::randomGenericMap));
    }

    public McpResourcesListChangedNotification randomResourcesListChangedNotification() {
        return new McpResourcesListChangedNotification(mayBeNull(this::randomGenericMap));
    }

    public McpSamplingMessage randomSamplingMessage() {
        return new McpSamplingMessage(randomFrom(McpRole.values()), randomContent(), mayBeNull(this::randomGenericMap));
    }

    public McpToolsListChangedNotification randomToolsListChangedNotification() {
        return new McpToolsListChangedNotification(randomGenericMap());
    }

    public McpRootsListChangedNotification randomRootsListChangedNotification() {
        return new McpRootsListChangedNotification(randomGenericMap());
    }

    public McpResourcesUpdatedNotification randomResourcesUpdatedNotification() {
        return new McpResourcesUpdatedNotification(randomAlphaOfLength(10), randomGenericMap());
    }

    public McpSubscribeRequest randomSubscribeRequest() {
        return new McpSubscribeRequest(randomAlphaOfLength(10), mayBeNull(this::randomGenericMap));
    }

    protected <V> V mayBeNull(Supplier<V> supplier) {
        return randomBoolean() ? supplier.get() : null;
    }

    @Override
    protected NamedWriteableRegistry getNamedWriteableRegistry() {
        return new NamedWriteableRegistry(McpProtocol.namedWriteables());
    }

    @Override
    protected NamedXContentRegistry xContentRegistry() {
        return new NamedXContentRegistry(McpProtocol.namedXContent());
    }
}
