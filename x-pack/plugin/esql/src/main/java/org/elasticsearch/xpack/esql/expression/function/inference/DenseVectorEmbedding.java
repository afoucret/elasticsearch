/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.esql.expression.function.inference;

import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xpack.esql.core.expression.Attribute;
import org.elasticsearch.xpack.esql.core.expression.Expression;
import org.elasticsearch.xpack.esql.core.expression.ReferenceAttribute;
import org.elasticsearch.xpack.esql.core.expression.UnresolvedAttribute;
import org.elasticsearch.xpack.esql.core.tree.NodeInfo;
import org.elasticsearch.xpack.esql.core.tree.Source;
import org.elasticsearch.xpack.esql.core.type.DataType;
import org.elasticsearch.xpack.esql.expression.function.FunctionInfo;
import org.elasticsearch.xpack.esql.expression.function.MapParam;
import org.elasticsearch.xpack.esql.expression.function.OptionalArgument;
import org.elasticsearch.xpack.esql.expression.function.Param;
import org.elasticsearch.xpack.esql.io.stream.PlanStreamInput;
import org.elasticsearch.xpack.esql.plan.logical.Eval;
import org.elasticsearch.xpack.esql.plan.logical.LogicalPlan;
import org.elasticsearch.xpack.esql.plan.logical.inference.InferencePlan;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DenseVectorEmbedding extends InferenceFunction implements OptionalArgument {

    public static final NamedWriteableRegistry.Entry ENTRY = new NamedWriteableRegistry.Entry(Expression.class, "DenseVectorEmbedding", Completion::new);

    private final Expression text;

    private final Attribute tmpAttribute;

    @FunctionInfo(
        returnType = "double",
        description = "Compute text similarity score using an inference model."
    )
    public DenseVectorEmbedding(
        Source source,
        @Param(name = "text", type = { "keyword", "text" }, description = "Prompt") Expression text,
        @MapParam(
            name = "options",
            params = {
                @MapParam.MapParamEntry(
                    name = "inference_id",
                    type = "keyword",
                    valueHint = { ".rerank-v1-elasticsearch" },
                    description = "Inference endpoint to use"
                )
            },
            optional = true
        ) Expression options
    ) {
        super(source, options, List.of(text, options));
        this.text = text;
        this.tmpAttribute = new ReferenceAttribute(Source.EMPTY, ENTRY.name + "_" + UUID.randomUUID(), DataType.DENSE_VECTOR);
    }

    public DenseVectorEmbedding(StreamInput in) throws IOException {
        this(
            Source.readFrom((PlanStreamInput) in),
            in.readNamedWriteable(Expression.class),
            in.readNamedWriteable(Expression.class)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        source().writeTo(out);
        out.writeNamedWriteable(text);
        out.writeNamedWriteable(options());
    }

    @Override
    public DataType dataType() {
        return DataType.DENSE_VECTOR;
    }

    @Override
    public Expression replaceChildren(List<Expression> newChildren) {
        return new Completion(source(), newChildren.get(0), newChildren.size() > 1 ? newChildren.get(1) : null);
    }

    @Override
    protected NodeInfo<? extends Expression> info() {
        return NodeInfo.create(this, Completion::new, text, options());
    }

    @Override
    public String getWriteableName() {
        return ENTRY.name;
    }

    @Override
    public LogicalPlan rewriteInferenceFunctionToLogicalPlan(LogicalPlan plan) {
        if (plan instanceof Eval eval) {
            InferencePlan<?> embedding = new org.elasticsearch.xpack.esql.plan.logical.inference.DenseVectorEmbedding(Source.EMPTY, eval.child(), inferenceId(), text, tmpAttribute);
            plan = eval.replaceChild(embedding).transformExpressionsDown(Completion.class, embeddingFunction -> embeddingFunction.equals(this) ? new UnresolvedAttribute(Source.EMPTY, tmpAttribute.name()) : embeddingFunction);
        }

        return plan;
    }

    @Override
    public List<Attribute> temporaryAttributes() {
        return List.of(tmpAttribute);
    }
}
