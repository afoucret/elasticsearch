/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.ml.queries;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.elasticsearch.TransportVersion;
import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.mapper.MappedFieldType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.MatchNoneQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryRewriteContext;
import org.elasticsearch.index.query.SearchExecutionContext;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TermStatsScriptQueryBuilder extends AbstractQueryBuilder<TermStatsScriptQueryBuilder> {

    public static final String NAME = "term_stats_script";

    private static final ParseField INNER_FILTER_FIELD = new ParseField("filter");
    private static final ParseField FIELD_NAME_FIELD = new ParseField("field");
    private static final ParseField QUERY_FIELD = new ParseField("query");
    private static final ParseField ANALYZER_FIELD = new ParseField("analyzer");

    private final QueryBuilder filterBuilder;
    private final String field;
    private final String query;
    private final String analyzer;

    public TermStatsScriptQueryBuilder(String field, String query, String analyzer, QueryBuilder filterBuilder) {
        this.field = Strings.requireNonBlank(field, "field name can not be null or blank");
        this.query = Objects.requireNonNull(query, "query can not be null");
        this.analyzer = analyzer;
        this.filterBuilder = Objects.requireNonNull(filterBuilder, "inner clause [filter] cannot be null.");
    }

    public TermStatsScriptQueryBuilder(StreamInput in) throws IOException {
        super(in);
        field = in.readString();
        query = in.readString();
        analyzer = in.readOptionalString();
        filterBuilder = in.readNamedWriteable(QueryBuilder.class);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    public TransportVersion getMinimalSupportedVersion() {
        // TODO: bump a new transport version before merging.
        return TransportVersion.current();
    }

    @Override
    protected void doWriteTo(StreamOutput out) throws IOException {
        out.writeString(field);
        out.writeString(query);
        out.writeOptionalString(analyzer);
        out.writeNamedWriteable(filterBuilder);
    }

    @Override
    protected QueryBuilder doRewrite(QueryRewriteContext queryRewriteContext) throws IOException {
        QueryBuilder rewrite = filterBuilder.rewrite(queryRewriteContext);

        if (rewrite instanceof MatchNoneQueryBuilder) {
            return rewrite; // we won't match anyway
        }

        if (rewrite != filterBuilder) {
            return new TermStatsScriptQueryBuilder(field, query, analyzer, rewrite);
        }

        return this;
    }

    public static TermStatsScriptQueryBuilder fromXContent(XContentParser parser) throws IOException {
        String field = null;
        boolean hasField = false;

        String query = null;
        boolean hasQuery = false;

        String analyzer = null;

        QueryBuilder innerFilter = QueryBuilders.matchAllQuery();
        String queryName = null;
        float boost = AbstractQueryBuilder.DEFAULT_BOOST;

        String currentFieldName = null;
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                if (INNER_FILTER_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    innerFilter = parseInnerQueryBuilder(parser);
                } else {
                    throw new ParsingException(
                        parser.getTokenLocation(),
                        "[" + NAME + "] query does not support [" + currentFieldName + "]"
                    );
                }
            } else if (token.isValue()) {
                if (AbstractQueryBuilder.NAME_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    queryName = parser.text();
                } else if (AbstractQueryBuilder.BOOST_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    boost = parser.floatValue();
                }  else if (FIELD_NAME_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    field = parser.text();
                    hasField = true;
                } else if (QUERY_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    query = parser.text();
                    hasQuery = true;
                } else if (ANALYZER_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    analyzer = parser.text();
                } else {
                    throw new ParsingException(
                        parser.getTokenLocation(),
                        "[" + NAME + "] query does not support [" + currentFieldName + "]"
                    );
                }
            } else {
                throw new ParsingException(parser.getTokenLocation(), "unexpected token [" + token + "]");
            }
        }

        if (hasField == false) {
            throw new ParsingException(parser.getTokenLocation(), "[" + NAME + "] requires a 'field' element");
        }

        if (hasQuery == false) {
            throw new ParsingException(parser.getTokenLocation(), "[" + NAME + "] requires a 'query' element");
        }

        TermStatsScriptQueryBuilder termStatsScriptQueryBuilder = new TermStatsScriptQueryBuilder(field, query, analyzer, innerFilter);
        termStatsScriptQueryBuilder.boost(boost);
        termStatsScriptQueryBuilder.queryName(queryName);

        return termStatsScriptQueryBuilder;
    }

    @Override
    protected void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(NAME);
        {
            builder.field(FIELD_NAME_FIELD.getPreferredName(), field);
            builder.field(QUERY_FIELD.getPreferredName(), query);
            builder.field(ANALYZER_FIELD.getPreferredName(), analyzer);
            builder.field(INNER_FILTER_FIELD.getPreferredName());
            filterBuilder.toXContent(builder, params);
            printBoostAndQueryName(builder);
        }
        builder.endObject();
    }

    @Override
    protected Query doToQuery(SearchExecutionContext context) throws IOException {
        Set<Term> terms = extractTerms(context);
        Query innerFilter = filterBuilder.toQuery(context);

        return new TermStatsScriptQuery(innerFilter, terms);
    }

    @Override
    protected boolean doEquals(TermStatsScriptQueryBuilder other) {
        return Objects.equals(field, other.field)
            && Objects.equals(query, other.query)
            && Objects.equals(analyzer, other.analyzer)
            && Objects.equals(filterBuilder, other.filterBuilder);
    }

    @Override
    protected int doHashCode() {
        return Objects.hash(field, query, analyzer, filterBuilder);
    }

    private Set<Term> extractTerms(SearchExecutionContext context) throws IOException {
        Set<Term> terms = new HashSet<>();
        Analyzer analyzer = getAnalyzer(context);

        try (TokenStream ts = analyzer.tokenStream(field, query)) {
            TermToBytesRefAttribute termAttr = ts.getAttribute(TermToBytesRefAttribute.class);
            ts.reset();
            while (ts.incrementToken()) {
                terms.add(new Term(field, termAttr.getBytesRef()));
            }
        }

        return terms;
    }

    private Analyzer getAnalyzer(SearchExecutionContext context) {
        if (analyzer != null) {
            return context.getIndexAnalyzers().get(analyzer);
        }

        // TODO: additional checks on the field.
        MappedFieldType fieldType = context.getFieldType(field);
        return fieldType.getTextSearchInfo().searchAnalyzer();
    }
}
