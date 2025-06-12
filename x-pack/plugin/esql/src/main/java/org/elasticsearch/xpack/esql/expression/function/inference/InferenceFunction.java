/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.esql.expression.function.inference;

import org.elasticsearch.xpack.esql.core.InvalidArgumentException;
import org.elasticsearch.xpack.esql.core.expression.Expression;
import org.elasticsearch.xpack.esql.core.expression.Literal;
import org.elasticsearch.xpack.esql.core.expression.MapExpression;
import org.elasticsearch.xpack.esql.core.expression.TypeResolutions;
import org.elasticsearch.xpack.esql.core.expression.function.Function;
import org.elasticsearch.xpack.esql.core.tree.Source;
import org.elasticsearch.xpack.esql.core.type.DataType;
import org.elasticsearch.xpack.esql.plan.logical.LogicalPlan;
import org.elasticsearch.xpack.esql.plan.logical.inference.Rerank;

import java.util.List;
import java.util.stream.Stream;

public abstract class InferenceFunction extends Function {

    private final Expression inferenceId;

    private final Expression options;

    @SuppressWarnings("this-escape")
    public <E> InferenceFunction(Source source, Expression options, List<Expression> children) {
        super(source, Stream.concat(children.stream(), options == null ?  Stream.of() : Stream.of(options)).toList());
        this.options = options;
        this.inferenceId = inferenceIdFromOptions(this, options);
    }

    public Expression options() {
        return options;
    }

    public Expression inferenceId() {
        return inferenceId;
    }

    public abstract LogicalPlan rewriteInferenceFunctionToLogicalPlan(LogicalPlan plan);

    private static Expression inferenceIdFromOptions(InferenceFunction f, Expression options) {
        if (options != null) {
            TypeResolution resolution = TypeResolutions.isMapExpression(options, f.functionName(), TypeResolutions.ParamOrdinal.THIRD);

            if (resolution.unresolved()) {
                throw new InvalidArgumentException(resolution.message());
            }

            MapExpression mapOptions = (MapExpression) options;
            Expression value = mapOptions.get("inference_id");
            if (value != null) {
                return value;
            }
        }

        return new Literal(Source.EMPTY, Rerank.DEFAULT_INFERENCE_ID, DataType.KEYWORD);
    }
}
