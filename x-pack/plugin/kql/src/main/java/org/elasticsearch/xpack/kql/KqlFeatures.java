/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.kql;

import org.elasticsearch.Build;
import org.elasticsearch.common.util.set.Sets;
import org.elasticsearch.features.FeatureSpecification;
import org.elasticsearch.features.NodeFeature;
import org.elasticsearch.xpack.kql.query.KqlQueryBuilder;

import java.util.Collections;
import java.util.Set;

public class KqlFeatures implements FeatureSpecification {
    @Override
    public Set<NodeFeature> getFeatures() {
        Set<NodeFeature> features = Set.of();

        if (Build.current().isSnapshot()) {
            return Collections.unmodifiableSet(Sets.union(features, snapshotFeatures()));
        }

        return features;
    }

    private Set<NodeFeature> snapshotFeatures() {
        return Set.of(KqlQueryBuilder.KQL_QUERY_SUPPORTED);
    }
}
