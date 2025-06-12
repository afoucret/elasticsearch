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
import org.elasticsearch.xpack.esql.core.expression.Alias;
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
import org.elasticsearch.xpack.esql.plan.logical.OrderBy;
import org.elasticsearch.xpack.esql.plan.logical.UnaryPlan;
import org.elasticsearch.xpack.esql.plan.logical.inference.Rerank;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class TextSimilarity extends InferenceFunction implements OptionalArgument {

    public static final NamedWriteableRegistry.Entry ENTRY = new NamedWriteableRegistry.Entry(Expression.class, "TextSimilarity", TextSimilarity::new);

    private final Expression queryText;

    private final Expression rerankExpression;

    private final Attribute tmpAttribute;


    @FunctionInfo(
        returnType = "double",
        description = "Compute text similarity score using an inference model."
    )
    public TextSimilarity(
        Source source,
        @Param(name = "rerankExpression", type = { "keyword", "text" }, description = "Fields used") Expression rerankExpression,
        @Param(name = "queryText", type = { "keyword", "text" }, description = "The query.") Expression queryText,
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
        super(source, options, List.of(rerankExpression, queryText));
        this.queryText = queryText;
        this.rerankExpression = rerankExpression;
        this.tmpAttribute = new ReferenceAttribute(Source.EMPTY, ENTRY.name + "_" + UUID.randomUUID(), DataType.DOUBLE);
    }

    public TextSimilarity(StreamInput in) throws IOException {
        this(
            Source.readFrom((PlanStreamInput) in),
            in.readNamedWriteable(Expression.class),
            in.readNamedWriteable(Expression.class),
            in.readNamedWriteable(Expression.class)
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        source().writeTo(out);
        out.writeNamedWriteable(queryText);
        out.writeNamedWriteable(rerankExpression);
        out.writeNamedWriteable(options());
    }

    @Override
    public DataType dataType() {
        return DataType.DOUBLE;
    }

    @Override
    public Expression replaceChildren(List<Expression> newChildren) {
        return new TextSimilarity(source(), newChildren.get(0), newChildren.get(1), newChildren.size() > 2 ? newChildren.get(2) : null);
    }

    @Override
    protected NodeInfo<? extends Expression> info() {
        return NodeInfo.create(this, TextSimilarity::new, queryText, rerankExpression, options());
    }

    @Override
    public String getWriteableName() {
        return ENTRY.name;
    }

    public List<Attribute> temporaryAttributes() {
        return List.of(tmpAttribute);
    }

    @Override
    public LogicalPlan rewriteInferenceFunctionToLogicalPlan(LogicalPlan plan) {
        if (plan instanceof UnaryPlan unary && (plan instanceof OrderBy || plan instanceof Eval)) {
            Rerank rerank = new Rerank(Source.EMPTY, unary.child(), inferenceId(), queryText, List.of(new Alias(rerankExpression.source(), rerankExpression.source().text(), rerankExpression))).withScoreAttribute(tmpAttribute);
            plan = unary.replaceChild(rerank).transformExpressionsDown(TextSimilarity.class, textSimilarity -> textSimilarity.equals(this) ? new UnresolvedAttribute(Source.EMPTY, tmpAttribute.name()) : textSimilarity);
        }

        return plan;
    }
}
