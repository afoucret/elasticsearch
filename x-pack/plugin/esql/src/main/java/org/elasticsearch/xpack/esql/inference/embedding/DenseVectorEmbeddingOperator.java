/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.esql.inference.embedding;

import org.elasticsearch.compute.data.FloatBlock;
import org.elasticsearch.compute.data.Page;
import org.elasticsearch.compute.operator.DriverContext;
import org.elasticsearch.compute.operator.EvalOperator;
import org.elasticsearch.compute.operator.Operator;
import org.elasticsearch.core.Releasables;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.xpack.esql.inference.InferenceOperator;
import org.elasticsearch.xpack.esql.inference.InferenceRunner;
import org.elasticsearch.xpack.esql.inference.bulk.BulkInferenceExecutionConfig;
import org.elasticsearch.xpack.esql.inference.bulk.BulkInferenceRequestIterator;

import java.util.stream.IntStream;

public class DenseVectorEmbeddingOperator extends InferenceOperator {

    private static final int DEFAULT_BATCH_SIZE = 20;

    private final EvalOperator.ExpressionEvaluator inputEvaluator;

    // Batch size used to group rows into a single inference request (currently fixed)
    // TODO: make it configurable either in the command or as query pragmas
    private final int batchSize = DEFAULT_BATCH_SIZE;

    public DenseVectorEmbeddingOperator(
        DriverContext driverContext,
        InferenceRunner inferenceRunner,
        ThreadPool threadPool,
        String inferenceId,
        EvalOperator.ExpressionEvaluator inputEvaluator
    ) {
        super(driverContext, inferenceRunner, BulkInferenceExecutionConfig.DEFAULT, threadPool, inferenceId);
        this.inputEvaluator = inputEvaluator;
    }

    @Override
    protected void doClose() {
        Releasables.close(inputEvaluator);
    }

    @Override
    public String toString() {
        return "DenseVectorEmbeddingOperator[inference_id=[" + inferenceId() + "]]";
    }

    @Override
    public void addInput(Page input) {
        try {
            super.addInput(input.appendBlock(inputEvaluator.eval(input)));
        } catch (Exception e) {
            releasePageOnAnyThread(input);
            throw e;
        }
    }

    @Override
    protected BulkInferenceRequestIterator requests(Page inputPage) {
        int inputBlockChannel = inputPage.getBlockCount() - 1;
        return new DenseVectorEmbeddingOperatorRequestIterator(inputPage.getBlock(inputBlockChannel), inferenceId(), batchSize);
    }


    @Override
    protected DenseVectorEmbeddingOperatorOutputBuilder outputBuilder(Page input) {
        FloatBlock.Builder outputBlockBuilder = blockFactory().newFloatBlockBuilder(input.getPositionCount());
        return new DenseVectorEmbeddingOperatorOutputBuilder(
            outputBlockBuilder,
            input.projectBlocks(IntStream.range(0, input.getBlockCount() - 1).toArray())
        );
    }

    public record Factory(InferenceRunner inferenceRunner, String inferenceId, EvalOperator.ExpressionEvaluator.Factory inputEvaluatorFactory)
        implements
        Operator.OperatorFactory {
        @Override
        public String describe() {
            return "CompletionOperator[inference_id=[" + inferenceId + "]]";
        }

        @Override
        public Operator get(DriverContext driverContext) {
            return new DenseVectorEmbeddingOperator(
                driverContext,
                inferenceRunner,
                inferenceRunner.threadPool(),
                inferenceId,
                inputEvaluatorFactory.get(driverContext)
            );
        }
    }
}
