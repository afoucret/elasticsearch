/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.esql.optimizer.rules.logical;

import org.elasticsearch.xpack.esql.plan.logical.LogicalPlan;
import org.elasticsearch.xpack.esql.plan.logical.inference.DenseVectorEmbedding;

public final class PushDownDenseVectorEmbedding extends OptimizerRules.OptimizerRule<DenseVectorEmbedding> {
    @Override
    protected LogicalPlan rule(DenseVectorEmbedding p) {
        return PushDownUtils.pushGeneratingPlanPastProjectAndOrderBy(p);
    }
}
