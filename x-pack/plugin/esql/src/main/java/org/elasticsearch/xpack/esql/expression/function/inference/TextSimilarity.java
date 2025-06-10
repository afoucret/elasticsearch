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
import org.elasticsearch.xpack.esql.core.expression.Expression;
import org.elasticsearch.xpack.esql.core.expression.function.Function;
import org.elasticsearch.xpack.esql.core.tree.NodeInfo;
import org.elasticsearch.xpack.esql.core.tree.Source;
import org.elasticsearch.xpack.esql.core.type.DataType;
import org.elasticsearch.xpack.esql.expression.function.FunctionInfo;
import org.elasticsearch.xpack.esql.expression.function.Param;
import org.elasticsearch.xpack.esql.io.stream.PlanStreamInput;

import java.io.IOException;
import java.util.List;

public class TextSimilarity extends Function implements InferenceFunction {

    public static final NamedWriteableRegistry.Entry ENTRY = new NamedWriteableRegistry.Entry(Expression.class, "TextSimilarity", TextSimilarity::new);

    private final Expression inferenceId;

    private final Expression queryText;

    private final Expression rerankExpression;


    @FunctionInfo(
        returnType = "double",
        description = "Compute text similarity score using an inference model."
    )
    public TextSimilarity(
        Source source,
        @Param(name = "inferenceId", type = { "keyword", "text" }, description = "The inference id.") Expression inferenceId,
        @Param(name = "queryText", type = { "keyword", "text" }, description = "The query.") Expression queryText,
        @Param(name = "rerankExpression", type = { "keyword", "text" }, description = "Fields used") Expression rerankExpression
    ) {
        super(source, List.of(inferenceId, queryText, rerankExpression));
        this.inferenceId = inferenceId;
        this.queryText = queryText;
        this.rerankExpression = rerankExpression;
    }

    private TextSimilarity(StreamInput in) throws IOException {
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
        out.writeNamedWriteable(inferenceId);
        out.writeNamedWriteable(queryText);
        out.writeNamedWriteable(rerankExpression);
    }
    @Override
    public DataType dataType() {
        return DataType.DOUBLE;
    }

    @Override
    public Expression replaceChildren(List<Expression> newChildren) {
        return new TextSimilarity(source(), newChildren.get(0), newChildren.get(1), newChildren.get(2));
    }

    @Override
    protected NodeInfo<? extends Expression> info() {
        return NodeInfo.create(this, TextSimilarity::new, children().get(0), children().get(1), children().get(2));
    }

    @Override
    public String getWriteableName() {
        return ENTRY.name;
    }

    @Override
    public Expression inferenceId() {
        return inferenceId();
    }
}
