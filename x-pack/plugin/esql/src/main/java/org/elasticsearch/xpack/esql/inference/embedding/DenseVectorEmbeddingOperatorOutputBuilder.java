/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.esql.inference.embedding;

import org.elasticsearch.compute.data.Block;
import org.elasticsearch.compute.data.FloatBlock;
import org.elasticsearch.compute.data.Page;
import org.elasticsearch.core.Releasables;
import org.elasticsearch.xpack.core.inference.action.InferenceAction;
import org.elasticsearch.xpack.core.inference.results.EmbeddingResults;
import org.elasticsearch.xpack.core.inference.results.TextEmbeddingByteResults;
import org.elasticsearch.xpack.core.inference.results.TextEmbeddingFloatResults;
import org.elasticsearch.xpack.core.inference.results.TextEmbeddingResults;
import org.elasticsearch.xpack.esql.inference.InferenceOperator;

public class DenseVectorEmbeddingOperatorOutputBuilder implements InferenceOperator.OutputBuilder {

    private final Page inputPage;
    private final FloatBlock.Builder vectorBlockBuilder;

    public DenseVectorEmbeddingOperatorOutputBuilder(FloatBlock.Builder vectorBlockBuilder, Page inputPage) {
        this.inputPage = inputPage;
        this.vectorBlockBuilder = vectorBlockBuilder;
    }

    @Override
    public void close() {
        Releasables.close(vectorBlockBuilder);
        releasePageOnAnyThread(inputPage);
    }

    @Override
    public Page buildOutput() {
        Block outputBlock = vectorBlockBuilder.build();
        assert outputBlock.getPositionCount() == inputPage.getPositionCount();
        return inputPage.shallowCopy().appendBlock(outputBlock);
    }

    @Override
    public void addInferenceResponse(InferenceAction.Response inferenceResponse) {
        for (EmbeddingResults.Embedding<?> embedding: inferenceResults(inferenceResponse).embeddings()) {

            switch(embedding) {
                case TextEmbeddingFloatResults.Embedding floatEmbedding -> {
                    vectorBlockBuilder.beginPositionEntry();
                    for (int i=0; i < floatEmbedding.values().length; i++) {
                        vectorBlockBuilder.appendFloat(floatEmbedding.values()[i]);
                    }
                    vectorBlockBuilder.endPositionEntry();
                }
                case TextEmbeddingByteResults.Embedding bytesEmbedding -> {
                    vectorBlockBuilder.beginPositionEntry();
                    for (int i=0; i < bytesEmbedding.values().length; i++) {
                        vectorBlockBuilder.appendFloat(bytesEmbedding.values()[i]);
                    }
                    vectorBlockBuilder.endPositionEntry();
                }
                default -> vectorBlockBuilder.appendNull();
            }
        }
    }

    private TextEmbeddingResults<?> inferenceResults(InferenceAction.Response inferenceResponse) {
        return InferenceOperator.OutputBuilder.inferenceResults(inferenceResponse, TextEmbeddingResults.class);
    }
}
