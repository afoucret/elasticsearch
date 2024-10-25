/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.kql.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.logging.log4j.util.Strings;
import org.apache.lucene.queryparser.classic.QueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ParserUtils {

    private static final String UNQUOTED_LITERAL_TERM_DELIMITER = " ";
    private static final char ESCAPE_CHAR = '\\';
    private static final char QUOTE_CHAR = '"';

    private ParserUtils() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T typedParsing(ParseTreeVisitor<?> visitor, ParserRuleContext ctx, Class<T> type) {
        Object result = ctx.accept(visitor);

        if (type.isInstance(result)) {
            return (T) result;
        }

        throw new KqlParsingException(
            "Invalid query '{}'[{}] given; expected {} but found {}",
            ctx.start.getLine(),
            ctx.start.getCharPositionInLine(),
            ctx.getText(),
            ctx.getClass().getSimpleName(),
            type.getSimpleName(),
            (result != null ? result.getClass().getSimpleName() : "null")
        );
    }

    public static String extractText(ParserRuleContext ctx) {
        return String.join(UNQUOTED_LITERAL_TERM_DELIMITER, extractTextTokems(ctx));
    }

    public static boolean hasWildcard(ParserRuleContext ctx) {
        return ctx.children.stream().anyMatch(childNode -> {
            if (childNode instanceof TerminalNode terminalNode) {
                Token token = terminalNode.getSymbol();
                return switch (token.getType()) {
                    case KqlBaseParser.WILDCARD -> true;
                    case KqlBaseParser.UNQUOTED_LITERAL -> token.getText().matches("[^\\\\]*[*].*");
                    default -> false;
                };
            }

            return false;
        });
    }

    public static String escapeQueryString(String queryText, boolean preseveWildcards) {
        if (preseveWildcards) {
            return Stream.of(queryText.split("[*]]")).map(QueryParser::escape).collect(Collectors.joining("*"));
        }

        return QueryParser.escape(queryText);
    }

    private static List<String> extractTextTokems(ParserRuleContext ctx) {
        assert ctx.children != null;
        List<String> textTokens = new ArrayList<>(ctx.children.size());

        for (ParseTree currentNode : ctx.children) {
            if (currentNode instanceof TerminalNode terminalNode) {
                textTokens.add(extractText(terminalNode));
            } else {
                throw new KqlParsingException("Unable to extract text from ctx", ctx.start.getLine(), ctx.start.getCharPositionInLine());
            }
        }

        return textTokens;
    }

    private static String extractText(TerminalNode node) {
        if (node.getSymbol().getType() == KqlBaseParser.QUOTED_STRING) {
            return unescapeQuotedString(node);
        } else if (node.getSymbol().getType() == KqlBaseParser.UNQUOTED_LITERAL) {
            return unescapeUnquotedLiteral(node);
        }

        return node.getText();
    }

    private static String unescapeQuotedString(TerminalNode ctx) {
        String inputText = ctx.getText();

        assert inputText.length() >= 2 && inputText.charAt(0) == QUOTE_CHAR && inputText.charAt(inputText.length() - 1) == QUOTE_CHAR;
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < inputText.length() - 1;) {
            char currentChar = inputText.charAt(i++);
            if (currentChar == ESCAPE_CHAR && i + 1 < inputText.length()) {
                currentChar = inputText.charAt(i++);
                switch (currentChar) {
                    case 't' -> sb.append('\t');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 'u' -> i = handleUnicodeSequemce(ctx, sb, inputText, i);
                    case QUOTE_CHAR -> sb.append('\"');
                    case ESCAPE_CHAR -> sb.append(ESCAPE_CHAR);
                    default -> sb.append(ESCAPE_CHAR).append(currentChar);
                }
            } else {
                sb.append(currentChar);
            }
        }

        return sb.toString();
    }

    private static String unescapeUnquotedLiteral(TerminalNode ctx) {
        String inputText = ctx.getText();

        if (inputText == null || inputText.isEmpty()) {
            return inputText;
        }
        StringBuilder sb = new StringBuilder(inputText.length());

        for (int i = 0; i < inputText.length();) {
            char currentChar = inputText.charAt(i++);
            if (currentChar == ESCAPE_CHAR && i < inputText.length()) {
                if (isEscapedKeywordSequence(inputText, i)) {
                    String sequence = handleKeywordSequence(inputText, i);
                    sb.append(sequence);
                    i += sequence.length();
                } else {
                    currentChar = inputText.charAt(i++);
                    switch (currentChar) {
                        case 't' -> sb.append('\t');
                        case 'n' -> sb.append('\n');
                        case 'r' -> sb.append('\r');
                        case 'u' -> i = handleUnicodeSequemce(ctx, sb, inputText, i);
                        case QUOTE_CHAR -> sb.append('\"');
                        case ESCAPE_CHAR -> sb.append(ESCAPE_CHAR);
                        case '(', ')', ':', '<', '>', '*', '{', '}' -> sb.append(currentChar);
                        default -> sb.append(ESCAPE_CHAR).append(currentChar);
                    }
                }
            } else {
                sb.append(currentChar);
            }
        }

        return sb.toString();
    }

    private static boolean isEscapedKeywordSequence(String input, int startIndex) {
        if (startIndex + 1 >= input.length()) {
            return false;
        }
        String remaining = Strings.toRootLowerCase(input.substring(startIndex));
        return remaining.startsWith("and") || remaining.startsWith("or") || remaining.startsWith("not");
    }

    private static String handleKeywordSequence(String input, int startIndex) {
        String remaining = input.substring(startIndex);
        if (Strings.toRootLowerCase(remaining).startsWith("and")) return remaining.substring(0, 3);
        if (Strings.toRootLowerCase(remaining).startsWith("or")) return remaining.substring(0, 2);
        if (Strings.toRootLowerCase(remaining).startsWith("not")) return remaining.substring(0, 3);
        return "";
    }

    private static int handleUnicodeSequemce(TerminalNode ctx, StringBuilder sb, String text, int startIdx) {
        int endIdx = startIdx + 4;
        String hex = text.substring(startIdx, endIdx);

        try {
            int code = Integer.parseInt(hex, 16);

            if (code >= 0xD800 && code <= 0xDFFF) {
                // U+D800—U+DFFF can only be used as surrogate pairs and are not valid character codes.
                throw new KqlParsingException(
                    "Invalid unicode character code, [{}] is a surrogate code",
                    ctx.getSymbol().getLine(),
                    ctx.getSymbol().getCharPositionInLine() + startIdx,
                    hex
                );
            }
            sb.append(String.valueOf(Character.toChars(code)));
        } catch (IllegalArgumentException e) {
            throw new KqlParsingException(
                "Invalid unicode character code [{}]",
                ctx.getSymbol().getLine(),
                ctx.getSymbol().getCharPositionInLine() + startIdx,
                hex
            );
        }

        return endIdx;
    }
}
