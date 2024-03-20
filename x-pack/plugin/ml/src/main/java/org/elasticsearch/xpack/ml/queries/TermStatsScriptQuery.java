/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.ml.queries;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryVisitor;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TwoPhaseIterator;
import org.apache.lucene.search.Weight;

import java.io.IOException;e
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TermStatsScriptQuery extends Query {
    private final Query filter;

    private final Set<Term> terms;

    private TermStatsCollector

    public TermStatsScriptQuery(Query filter, Set<Term> terms) {
        this.filter = Objects.requireNonNull(filter, "Filter must not be null");
        this.terms = Objects.requireNonNull(terms, "Filter must not be null");
    }

    @Override
    public String toString(String field) {
        return "TermStatsScript(" + this.filter.toString(field) + ", terms:  " + terms + ")";
    }

    @Override
    public void visit(QueryVisitor visitor) {
        this.filter.visit(visitor.getSubVisitor(BooleanClause.Occur.FILTER, this));
    }

    public Weight createWeight(IndexSearcher searcher, final ScoreMode scoreMode, float boost) throws IOException {
        Weight innerWeight = filter.createWeight(searcher, ScoreMode.COMPLETE_NO_SCORES, boost);

        if (scoreMode.needsScores() == false) {
            return innerWeight;
        }

        return new Weight(this) {
            @Override
            public boolean isCacheable(LeafReaderContext leafReaderContext) {
                return false;
            }

            @Override
            public Explanation explain(LeafReaderContext leafReaderContext, int i) throws IOException {
                return null;
            }

            @Override
            public Scorer scorer(LeafReaderContext context) throws IOException {
                Scorer innerFilterScorer = innerWeight.scorer(context);
                if (innerFilterScorer == null) {
                    return null;
                }
                return new TermStatsScriptScorer(this, innerWeight.scorer(context), boost);
            }
        };
    }

    @Override
    public boolean equals(Object other) {
        if (this.sameClassAs(other)) {
            TermStatsScriptQuery that = (TermStatsScriptQuery) other;
            return this.filter.equals(that.filter) && this.terms.equals(that.terms);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filter, terms);
    }

    private static class TermStatsScriptScorer extends Scorer {
        private final Scorer subQueryScorer;
        private final float boost;

        TermStatsScriptScorer(Weight weight, Scorer subQueryScorer, float boost) {
            super(weight);
            this.subQueryScorer = subQueryScorer;
            this.boost = boost;
        }

        @Override
        public float score() throws IOException {
            return 12.0f * boost;
        }

        @Override
        public int docID() {
            return subQueryScorer.docID();
        }

        @Override
        public DocIdSetIterator iterator() {
            return subQueryScorer.iterator();
        }

        @Override
        public TwoPhaseIterator twoPhaseIterator() {
            return subQueryScorer.twoPhaseIterator();
        }

        @Override
        public float getMaxScore(int upTo) {
            return Float.MAX_VALUE;
        }
    }

    private static class TermStatsCollector {
        private final IndexSearcher searcher;
        private final LeafReaderContext context;
        private final Set<Term> terms;

        public void collect(int docId) {
            for (Term term : terms) {
                if (docId == DocIdSetIterator.NO_MORE_DOCS) {
                    break;

                }
            }
        }

        private Map<String, Double> stats(List<Double> samples) {
            double count = samples.size();
            double sum = samples.stream().collect(Collectors.summingDouble(Double::doubleValue));
            double min = samples.stream().min(Comparator.comparingDouble(Double::doubleValue)).orElse(0.0d);
            double max = samples.stream().max(Comparator.comparingDouble(Double::doubleValue)).orElse(0.0d);
            double mean = sum / count;
            double variance = 0;
            if (count > 1) {
                 variance = Math.sqrt(samples.stream().collect(Collectors.summingDouble((value) -> Math.pow(value - mean, 2))) / (count - 1));
            }
            return Map.ofEntries(
                Map.entry("count", count),
                Map.entry("min", min),
                Map.entry("max", max),
                Map.entry("sum", sum),
                Map.entry("mean", mean),
                Map.entry("variance", variance)
            );
        }
    }
}
