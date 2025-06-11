/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.esql.expression.function.inference;

import org.elasticsearch.xpack.esql.core.expression.Expression;
import org.elasticsearch.xpack.esql.core.expression.function.Function;
import org.elasticsearch.xpack.esql.core.tree.Source;
import org.elasticsearch.xpack.esql.plan.logical.LogicalPlan;

import java.util.List;
import java.util.stream.Stream;

public abstract class InferenceFunction extends Function {

    private final Expression inferenceId;

    public <E> InferenceFunction(Source source, Expression inferenceId, List<Expression> children) {
        super(source, Stream.concat(Stream.of(), children.stream()).toList());
        this.inferenceId = inferenceId;
    }

    public Expression inferenceId() {
        return inferenceId;
    }

    public abstract LogicalPlan rewriteInferenceFunctionToLogicalPlan(LogicalPlan plan);
}
