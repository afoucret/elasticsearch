/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.kql.parser;

import org.elasticsearch.core.Strings;
import org.elasticsearch.index.query.MatchNoneQueryBuilder;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class KqlParserFieldQueryTests extends AbstractKqlParserTestCase {

    public void testParseFieldQueryWithNoMatchingFields() {
        // Using an unquoted literal
        assertThat(parseKqlQuery(kqlFieldQuery("not_a_field", randomIdentifier())), isA(MatchNoneQueryBuilder.class));
        assertThat(parseKqlQuery(kqlFieldQuery("not_a_field", randomInt())), isA(MatchNoneQueryBuilder.class));
        assertThat(parseKqlQuery(kqlFieldQuery("not_a_field", quoteString(randomIdentifier()))), isA(MatchNoneQueryBuilder.class));

        // Using a quoted string as field name
        assertThat(parseKqlQuery(kqlFieldQuery(quoteString("not_a_field"), randomIdentifier())), isA(MatchNoneQueryBuilder.class));
        assertThat(parseKqlQuery(kqlFieldQuery(quoteString("not_a_field"), randomInt())), isA(MatchNoneQueryBuilder.class));
        assertThat(
            parseKqlQuery(kqlFieldQuery(quoteString("not_a_field"), quoteString(randomIdentifier()))),
            isA(MatchNoneQueryBuilder.class)
        );

        // Not expanding wildcard with quoted string
        assertThat(parseKqlQuery(kqlFieldQuery(quoteString("mapped_*"), randomIdentifier())), isA(MatchNoneQueryBuilder.class));
        assertThat(parseKqlQuery(kqlFieldQuery(quoteString("mapped_*"), randomInt())), isA(MatchNoneQueryBuilder.class));
        assertThat(
            parseKqlQuery(kqlFieldQuery(quoteString("mapped_*"), quoteString(randomIdentifier()))),
            isA(MatchNoneQueryBuilder.class)
        );
    }

    public void testParseUnquotedLiteralKeywordFieldQuery() {
        // Single word
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo")), KEYWORD_FIELD_NAME, "foo");

        // Multiple words
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo bar")), KEYWORD_FIELD_NAME, "foo bar");

        // Escaped keywords
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo \\and bar")), KEYWORD_FIELD_NAME, "foo and bar");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo \\or bar")), KEYWORD_FIELD_NAME, "foo or bar");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "\\not foo bar")), KEYWORD_FIELD_NAME, "not foo bar");

        // Escaped characters
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo \\* bar")), KEYWORD_FIELD_NAME, "foo * bar");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo\\(bar\\)")), KEYWORD_FIELD_NAME, "foo(bar)");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo\\{bar\\}")), KEYWORD_FIELD_NAME, "foo{bar}");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo\\:bar")), KEYWORD_FIELD_NAME, "foo:bar");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo\\<bar")), KEYWORD_FIELD_NAME, "foo<bar");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo\\>bar")), KEYWORD_FIELD_NAME, "foo>bar");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo \\\\ bar")), KEYWORD_FIELD_NAME, "foo \\ bar");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo\\\"bar\\\"")), KEYWORD_FIELD_NAME, "foo\"bar\"");

        // Wrapping terms into parentheses
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "(foo baz)")), KEYWORD_FIELD_NAME, "foo baz");

        // Trailing operators (AND, NOT, OR) are terms of the match query
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "bar AND")), KEYWORD_FIELD_NAME, "bar AND");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "bar OR")), KEYWORD_FIELD_NAME, "bar OR");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "bar NOT")), KEYWORD_FIELD_NAME, "bar NOT");

        // Leading operators (AND, OR) are terms of the match query
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "AND bar")), KEYWORD_FIELD_NAME, "AND bar");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "OR bar")), KEYWORD_FIELD_NAME, "OR bar");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "NOT bar")), KEYWORD_FIELD_NAME, "NOT bar");

        // Lone operators (AND, NOT, OR)
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "AND")), KEYWORD_FIELD_NAME, "AND");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "OR")), KEYWORD_FIELD_NAME, "OR");
        assertTermQueryBuilder(parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "NOT")), KEYWORD_FIELD_NAME, "NOT");


        // Check we can use quoted field name as well
        assertThat(
            parseKqlQuery(kqlFieldQuery(KEYWORD_FIELD_NAME, "foo")),
            equalTo(parseKqlQuery(kqlFieldQuery(quoteString(KEYWORD_FIELD_NAME), "foo")))
        );
    }

    public void testParseDateFieldQuery() {
        assertRangeQueryBuilder(parseKqlQuery(kqlFieldQuery(DATE_FIELD_NAME, "2010-06-03")), DATE_FIELD_NAME, (rangeQuery) -> {
            assertThat(rangeQuery.from(), equalTo("2010-06-03"));
            assertThat(rangeQuery.includeLower(), equalTo(true));
            assertThat(rangeQuery.to(), equalTo("2010-06-03"));
            assertThat(rangeQuery.includeUpper(), equalTo(true));

            // Check we can use quoted field name as well
            assertThat(parseKqlQuery(kqlFieldQuery(quoteString(DATE_FIELD_NAME), "2010-06-03")), equalTo(rangeQuery));

            // Check we can use quoted value as well
            assertThat(parseKqlQuery(kqlFieldQuery(DATE_FIELD_NAME, quoteString("2010-06-03"))), equalTo(rangeQuery));
        });
    }

    public void testParseUnquotedLiteralMatchFieldsQuery() {
        for (String fieldName : List.of(TEXT_FIELD_NAME, INT_FIELD_NAME, DOUBLE_FIELD_NAME)) {
            // Single word
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo")), fieldName, "foo");

            // Numbers are converted to string
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, 1)), fieldName, "1");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, 1.5)), fieldName, "1.5");

            // Multiple words
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo bar")), fieldName, "foo bar");

            // Escaped keywords
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo \\and bar")), fieldName, "foo and bar");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo \\or bar")), fieldName, "foo or bar");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "\\not foo bar")), fieldName, "not foo bar");

            // Escaped characters
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo \\* bar")), fieldName, "foo * bar");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo\\(bar\\)")), fieldName, "foo(bar)");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo\\{bar\\}")), fieldName, "foo{bar}");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo\\:bar")), fieldName, "foo:bar");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo\\<bar")), fieldName, "foo<bar");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo\\>bar")), fieldName, "foo>bar");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo \\\\ bar")), fieldName, "foo \\ bar");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "foo\\\"bar\\\"")), fieldName, "foo\"bar\"");

            // Wrapping terms into parentheses
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "(foo baz)")), fieldName, "foo baz");

            // Trailing operators (AND, NOT, OR) are terms of the match query
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "bar AND")), fieldName, "bar AND");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "bar OR")), fieldName, "bar OR");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "bar NOT")), fieldName, "bar NOT");

            // Leading operators (AND, OR) are terms of the match query
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "AND bar")), fieldName, "AND bar");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "OR bar")), fieldName, "OR bar");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "NOT bar")), fieldName, "NOT bar");

            // Lone operators (AND, NOT, OR)
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "AND")), fieldName, "AND");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "OR")), fieldName, "OR");
            assertMatchQueryBuilder(parseKqlQuery(kqlFieldQuery(fieldName, "NOT")), fieldName, "NOT");


            // Check we can use quoted field name as well
            assertThat(
                parseKqlQuery(kqlFieldQuery(fieldName, "foo")),
                equalTo(parseKqlQuery(kqlFieldQuery(quoteString(fieldName), "foo")))
            );
        }
    }

    private static String kqlFieldQuery(String field, Object value) {
        return wrapWithRandomWhitespaces(field) + wrapWithRandomWhitespaces(":") + wrapWithRandomWhitespaces(value.toString());
    }

    private static String quoteString(String input) {
        return Strings.format("\"%s\"", input);
    }
}
