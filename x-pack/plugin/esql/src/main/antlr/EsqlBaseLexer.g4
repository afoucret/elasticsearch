/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
lexer grammar EsqlBaseLexer;

@header {
/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
}

options {
  superClass=LexerConfig;
  caseInsensitive=true;
}

/*
 * Before modifying this file, please read the section above as changes here
 * have significant impact in the ANTLR generated code and its consumption upstream
 * (including Kibana).
 *
 * A. To add a development token (only available behind in snapshot/dev builds)
 *
 * Since the tokens/modes are in development, simply define them under the
 * "// in development section" and follow the section comments in that section.
 * That is use the DEV_ prefix and use the {this.isDevVersion()}? conditional.
 * They are defined at the end of the file, to minimize the impact on the existing
 * token types.
 *
 * B. To add a new (production-ready) token
 *
 * Be sure to go through step A (add a development token).
 * Make sure to remove the prefix and conditional before promoting the tokens in
 * production.
 * Since tokens types (numbers) are generated by ANTLR in a continuous fashion,
 * it is desirable to avoid changing these values hence where possible, add
 * add them at the end of their respective section.
 * Note that the use of lexing modes prevents this since any addition to a mode
 * (regardless where it occurs) shifts all the declarations that follow in other modes.
 *
 * C. Renaming a token
 *
 * Avoid renaming the token. But if you really have to, please check with the
 * Kibana team as they might be using the generated ANTLR "dictionary".
 *
 * D. To remove a token
 *
 * If the tokens haven't made it to production (and make sure to double check),
 * simply remove them from the grammar.
 * If the tokens get promoted to release, check with the Kibana team the impact
 * they have on the UI (auto-completion, etc...)
 */

DISSECT : 'dissect'           -> pushMode(EXPRESSION_MODE);
DROP : 'drop'                 -> pushMode(PROJECT_MODE);
ENRICH : 'enrich'             -> pushMode(ENRICH_MODE);
EVAL : 'eval'                 -> pushMode(EXPRESSION_MODE);
EXPLAIN : 'explain'           -> pushMode(EXPLAIN_MODE);
FROM : 'from'                 -> pushMode(FROM_MODE);
GROK : 'grok'                 -> pushMode(EXPRESSION_MODE);
KEEP : 'keep'                 -> pushMode(PROJECT_MODE);
LIMIT : 'limit'               -> pushMode(EXPRESSION_MODE);
MV_EXPAND : 'mv_expand'       -> pushMode(MVEXPAND_MODE);
RENAME : 'rename'             -> pushMode(RENAME_MODE);
ROW : 'row'                   -> pushMode(EXPRESSION_MODE);
SHOW : 'show'                 -> pushMode(SHOW_MODE);
SORT : 'sort'                 -> pushMode(EXPRESSION_MODE);
STATS : 'stats'               -> pushMode(EXPRESSION_MODE);
WHERE : 'where'               -> pushMode(EXPRESSION_MODE);
//
// in development
//
// Before adding a new in-development command, to sandbox the behavior when running in production environments
//
// For example: to add myCommand use the following declaration:
// DEV_MYCOMMAND : {this.isDevVersion()}? 'mycommand' -> ...
//
// Once the command has been stabilized, remove the DEV_ prefix and the {}? conditional and move the command to the
// main section while preserving alphabetical order:
// MYCOMMAND : 'mycommand' -> ...
DEV_INLINESTATS : {this.isDevVersion()}? 'inlinestats'   -> pushMode(EXPRESSION_MODE);
DEV_LOOKUP :      {this.isDevVersion()}? 'lookup_🐔'      -> pushMode(LOOKUP_MODE);
DEV_METRICS :     {this.isDevVersion()}? 'metrics'       -> pushMode(METRICS_MODE);
// list of all JOIN commands
DEV_JOIN :        {this.isDevVersion()}? 'join'          -> pushMode(JOIN_MODE);
DEV_JOIN_FULL :   {this.isDevVersion()}? 'full'          -> pushMode(JOIN_MODE);
DEV_JOIN_LEFT :   {this.isDevVersion()}? 'left'          -> pushMode(JOIN_MODE);
DEV_JOIN_RIGHT :  {this.isDevVersion()}? 'right'         -> pushMode(JOIN_MODE);
DEV_JOIN_LOOKUP : {this.isDevVersion()}? 'lookup'        -> pushMode(JOIN_MODE);
DEV_COMPLETION : {this.isDevVersion()}? 'completion'     -> pushMode(EXPRESSION_MODE);



//
// Catch-all for unrecognized commands - don't define any beyond this line
//
UNKNOWN_CMD : ~[ \r\n\t[\]/]+ -> pushMode(EXPRESSION_MODE) ;

LINE_COMMENT
    : '//' ~[\r\n]* '\r'? '\n'? -> channel(HIDDEN)
    ;

MULTILINE_COMMENT
    : '/*' (MULTILINE_COMMENT|.)*? '*/' -> channel(HIDDEN)
    ;

WS
    : [ \r\n\t]+ -> channel(HIDDEN)
    ;

//
// Expression - used by most command
//
mode EXPRESSION_MODE;

PIPE : '|' -> popMode;

fragment DIGIT
    : [0-9]
    ;

fragment LETTER
    : [a-z]
    ;

fragment ESCAPE_SEQUENCE
    : '\\' [tnr"\\]
    ;

fragment UNESCAPED_CHARS
    : ~[\r\n"\\]
    ;

fragment EXPONENT
    : [e] [+-]? DIGIT+
    ;

fragment ASPERAND
    : '@'
    ;

fragment BACKQUOTE
    : '`'
    ;

fragment BACKQUOTE_BLOCK
    : ~'`'
    | '``'
    ;

fragment UNDERSCORE
    : '_'
    ;

fragment UNQUOTED_ID_BODY
    : (LETTER | DIGIT | UNDERSCORE)
    ;

QUOTED_STRING
    : '"' (ESCAPE_SEQUENCE | UNESCAPED_CHARS)* '"'
    | '"""' (~[\r\n])*? '"""' '"'? '"'?
    ;

INTEGER_LITERAL
    : DIGIT+
    ;

DECIMAL_LITERAL
    : DIGIT+ DOT DIGIT*
    | DOT DIGIT+
    | DIGIT+ (DOT DIGIT*)? EXPONENT
    | DOT DIGIT+ EXPONENT
    ;

BY : 'by';

AND : 'and';
ASC : 'asc';
ASSIGN : '=';
CAST_OP : '::';
COLON : ':';
COMMA : ',';
DESC : 'desc';
DOT : '.';
FALSE : 'false';
FIRST : 'first';
IN: 'in';
IS: 'is';
LAST : 'last';
LIKE: 'like';
LP : '(';
NOT : 'not';
NULL : 'null';
NULLS : 'nulls';
OR : 'or';
PARAM: '?';
RLIKE: 'rlike';
RP : ')';
TRUE : 'true';
WITH : 'with';
AS: 'as';

EQ  : '==';
CIEQ  : '=~';
NEQ : '!=';
LT  : '<';
LTE : '<=';
GT  : '>';
GTE : '>=';

PLUS : '+';
MINUS : '-';
ASTERISK : '*';
SLASH : '/';
PERCENT : '%';

NESTED_WHERE : WHERE -> type(WHERE);

NAMED_OR_POSITIONAL_PARAM
    : PARAM (LETTER | UNDERSCORE) UNQUOTED_ID_BODY*
    | PARAM DIGIT+
    ;

// Brackets are funny. We can happen upon a CLOSING_BRACKET in two ways - one
// way is to start in an explain command which then shifts us to expression
// mode. Thus, the two popModes on CLOSING_BRACKET. The other way could as
// the start of a multivalued field constant. To line up with the double pop
// the explain mode needs, we double push when we see that.
OPENING_BRACKET : '[' -> pushMode(EXPRESSION_MODE), pushMode(EXPRESSION_MODE);
CLOSING_BRACKET : ']' -> popMode, popMode;

UNQUOTED_IDENTIFIER
    : LETTER UNQUOTED_ID_BODY*
    // only allow @ at beginning of identifier to keep the option to allow @ as infix operator in the future
    // also, single `_` and `@` characters are not valid identifiers
    | (UNDERSCORE | ASPERAND) UNQUOTED_ID_BODY+
    ;

fragment QUOTED_ID
    : BACKQUOTE BACKQUOTE_BLOCK+ BACKQUOTE
    ;

QUOTED_IDENTIFIER
    : QUOTED_ID
    ;

EXPR_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

EXPR_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

EXPR_WS
    : WS -> channel(HIDDEN)
    ;

//
// Explain
//
mode EXPLAIN_MODE;
EXPLAIN_OPENING_BRACKET : OPENING_BRACKET -> type(OPENING_BRACKET), pushMode(DEFAULT_MODE);
EXPLAIN_PIPE : PIPE -> type(PIPE), popMode;
EXPLAIN_WS : WS -> channel(HIDDEN);
EXPLAIN_LINE_COMMENT : LINE_COMMENT -> channel(HIDDEN);
EXPLAIN_MULTILINE_COMMENT : MULTILINE_COMMENT -> channel(HIDDEN);

//
// FROM command
//
mode FROM_MODE;
FROM_PIPE : PIPE -> type(PIPE), popMode;
FROM_OPENING_BRACKET : OPENING_BRACKET -> type(OPENING_BRACKET);
FROM_CLOSING_BRACKET : CLOSING_BRACKET -> type(CLOSING_BRACKET);
FROM_COLON : COLON -> type(COLON);
FROM_COMMA : COMMA -> type(COMMA);
FROM_ASSIGN : ASSIGN -> type(ASSIGN);
METADATA : 'metadata';

// in 8.14 ` were not allowed
// this has been relaxed in 8.15 since " is used for quoting
fragment UNQUOTED_SOURCE_PART
    : ~[:"=|,[\]/ \t\r\n]
    | '/' ~[*/] // allow single / but not followed by another / or * which would start a comment -- used in index pattern date spec
    ;

UNQUOTED_SOURCE
    : UNQUOTED_SOURCE_PART+
    ;

FROM_UNQUOTED_SOURCE : UNQUOTED_SOURCE -> type(UNQUOTED_SOURCE);
FROM_QUOTED_SOURCE : QUOTED_STRING -> type(QUOTED_STRING);

FROM_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

FROM_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

FROM_WS
    : WS -> channel(HIDDEN)
    ;
//
// DROP, KEEP
//
mode PROJECT_MODE;
PROJECT_PIPE : PIPE -> type(PIPE), popMode;
PROJECT_DOT: DOT -> type(DOT);
PROJECT_COMMA : COMMA -> type(COMMA);
PROJECT_PARAM : {this.isDevVersion()}? PARAM -> type(PARAM);
PROJECT_NAMED_OR_POSITIONAL_PARAM : {this.isDevVersion()}? NAMED_OR_POSITIONAL_PARAM -> type(NAMED_OR_POSITIONAL_PARAM);

fragment UNQUOTED_ID_BODY_WITH_PATTERN
    : (LETTER | DIGIT | UNDERSCORE | ASTERISK)
    ;

fragment UNQUOTED_ID_PATTERN
    : (LETTER | ASTERISK) UNQUOTED_ID_BODY_WITH_PATTERN*
    | (UNDERSCORE | ASPERAND) UNQUOTED_ID_BODY_WITH_PATTERN+
    ;

ID_PATTERN
    : (UNQUOTED_ID_PATTERN | QUOTED_ID)+
    ;

PROJECT_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

PROJECT_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

PROJECT_WS
    : WS -> channel(HIDDEN)
    ;
//
// | RENAME a.b AS x, c AS y
//
mode RENAME_MODE;
RENAME_PIPE : PIPE -> type(PIPE), popMode;
RENAME_ASSIGN : ASSIGN -> type(ASSIGN);
RENAME_COMMA : COMMA -> type(COMMA);
RENAME_DOT: DOT -> type(DOT);
RENAME_PARAM : {this.isDevVersion()}? PARAM -> type(PARAM);
RENAME_NAMED_OR_POSITIONAL_PARAM : {this.isDevVersion()}? NAMED_OR_POSITIONAL_PARAM -> type(NAMED_OR_POSITIONAL_PARAM);

RENAME_AS : AS -> type(AS);

RENAME_ID_PATTERN
    : ID_PATTERN -> type(ID_PATTERN)
    ;

RENAME_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

RENAME_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

RENAME_WS
    : WS -> channel(HIDDEN)
    ;

// | ENRICH ON key WITH fields
mode ENRICH_MODE;
ENRICH_PIPE : PIPE -> type(PIPE), popMode;
ENRICH_OPENING_BRACKET : OPENING_BRACKET -> type(OPENING_BRACKET), pushMode(SETTING_MODE);

ON : 'on'     -> pushMode(ENRICH_FIELD_MODE);
ENRICH_WITH : WITH -> type(WITH), pushMode(ENRICH_FIELD_MODE);

// similar to that of an index
// see https://www.elastic.co/guide/en/elasticsearch/reference/current/indices-create-index.html#indices-create-api-path-params
fragment ENRICH_POLICY_NAME_BODY
    : ~[\\/?"<>| ,#\t\r\n:]
    ;

ENRICH_POLICY_NAME
    // allow prefix for the policy to specify its resolution
    : (ENRICH_POLICY_NAME_BODY+ COLON)? ENRICH_POLICY_NAME_BODY+
    ;

ENRICH_MODE_UNQUOTED_VALUE
    : ENRICH_POLICY_NAME -> type(ENRICH_POLICY_NAME)
    ;

ENRICH_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

ENRICH_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

ENRICH_WS
    : WS -> channel(HIDDEN)
    ;

// submode for Enrich to allow different lexing between policy source (loose) and field identifiers
mode ENRICH_FIELD_MODE;
ENRICH_FIELD_PIPE : PIPE -> type(PIPE), popMode, popMode;
ENRICH_FIELD_ASSIGN : ASSIGN -> type(ASSIGN);
ENRICH_FIELD_COMMA : COMMA -> type(COMMA);
ENRICH_FIELD_DOT: DOT -> type(DOT);

ENRICH_FIELD_WITH : WITH -> type(WITH) ;

ENRICH_FIELD_ID_PATTERN
    : ID_PATTERN -> type(ID_PATTERN)
    ;

ENRICH_FIELD_QUOTED_IDENTIFIER
    : QUOTED_IDENTIFIER -> type(QUOTED_IDENTIFIER)
    ;

ENRICH_FIELD_PARAM : {this.isDevVersion()}? PARAM -> type(PARAM);
ENRICH_FIELD_NAMED_OR_POSITIONAL_PARAM : {this.isDevVersion()}? NAMED_OR_POSITIONAL_PARAM -> type(NAMED_OR_POSITIONAL_PARAM);

ENRICH_FIELD_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

ENRICH_FIELD_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

ENRICH_FIELD_WS
    : WS -> channel(HIDDEN)
    ;

mode MVEXPAND_MODE;
MVEXPAND_PIPE : PIPE -> type(PIPE), popMode;
MVEXPAND_DOT: DOT -> type(DOT);
MVEXPAND_PARAM : {this.isDevVersion()}? PARAM -> type(PARAM);
MVEXPAND_NAMED_OR_POSITIONAL_PARAM : {this.isDevVersion()}? NAMED_OR_POSITIONAL_PARAM -> type(NAMED_OR_POSITIONAL_PARAM);

MVEXPAND_QUOTED_IDENTIFIER
    : QUOTED_IDENTIFIER -> type(QUOTED_IDENTIFIER)
    ;

MVEXPAND_UNQUOTED_IDENTIFIER
    : UNQUOTED_IDENTIFIER -> type(UNQUOTED_IDENTIFIER)
    ;

MVEXPAND_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

MVEXPAND_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

MVEXPAND_WS
    : WS -> channel(HIDDEN)
    ;

//
// SHOW commands
//
mode SHOW_MODE;
SHOW_PIPE : PIPE -> type(PIPE), popMode;

INFO : 'info';

SHOW_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

SHOW_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

SHOW_WS
    : WS -> channel(HIDDEN)
    ;

mode SETTING_MODE;
SETTING_CLOSING_BRACKET : CLOSING_BRACKET -> type(CLOSING_BRACKET), popMode;

SETTING_COLON : COLON -> type(COLON);

SETTING
    : (ASPERAND | DIGIT| DOT | LETTER | UNDERSCORE)+
    ;

SETTING_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

SETTTING_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

SETTING_WS
    : WS -> channel(HIDDEN)
    ;

//
// LOOKUP ON key
//
mode LOOKUP_MODE;
LOOKUP_PIPE : PIPE -> type(PIPE), popMode;
LOOKUP_COLON : COLON -> type(COLON);
LOOKUP_COMMA : COMMA -> type(COMMA);
LOOKUP_DOT: DOT -> type(DOT);
LOOKUP_ON : ON -> type(ON), pushMode(LOOKUP_FIELD_MODE);

LOOKUP_UNQUOTED_SOURCE: UNQUOTED_SOURCE -> type(UNQUOTED_SOURCE);
LOOKUP_QUOTED_SOURCE : QUOTED_STRING -> type(QUOTED_STRING);

LOOKUP_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

LOOKUP_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

LOOKUP_WS
    : WS -> channel(HIDDEN)
    ;

mode LOOKUP_FIELD_MODE;
LOOKUP_FIELD_PIPE : PIPE -> type(PIPE), popMode, popMode;
LOOKUP_FIELD_COMMA : COMMA -> type(COMMA);
LOOKUP_FIELD_DOT: DOT -> type(DOT);

LOOKUP_FIELD_ID_PATTERN
    : ID_PATTERN -> type(ID_PATTERN)
    ;

LOOKUP_FIELD_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

LOOKUP_FIELD_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

LOOKUP_FIELD_WS
    : WS -> channel(HIDDEN)
    ;

//
// JOIN-related commands
//
mode JOIN_MODE;
JOIN_PIPE : PIPE -> type(PIPE), popMode;
JOIN_JOIN : DEV_JOIN -> type(DEV_JOIN);
JOIN_AS : AS -> type(AS);
JOIN_ON : ON -> type(ON), popMode, pushMode(EXPRESSION_MODE);
USING : 'USING' -> popMode, pushMode(EXPRESSION_MODE);

JOIN_UNQUOTED_IDENTIFER: UNQUOTED_IDENTIFIER -> type(UNQUOTED_IDENTIFIER);
JOIN_QUOTED_IDENTIFIER : QUOTED_IDENTIFIER -> type(QUOTED_IDENTIFIER);

JOIN_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

JOIN_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

JOIN_WS
    : WS -> channel(HIDDEN)
    ;

//
// METRICS command
//
mode METRICS_MODE;
METRICS_PIPE : PIPE -> type(PIPE), popMode;

METRICS_UNQUOTED_SOURCE: UNQUOTED_SOURCE -> type(UNQUOTED_SOURCE), popMode, pushMode(CLOSING_METRICS_MODE);
METRICS_QUOTED_SOURCE : QUOTED_STRING -> type(QUOTED_STRING), popMode, pushMode(CLOSING_METRICS_MODE);

METRICS_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

METRICS_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

METRICS_WS
    : WS -> channel(HIDDEN)
    ;

// TODO: remove this workaround mode - see https://github.com/elastic/elasticsearch/issues/108528
mode CLOSING_METRICS_MODE;

CLOSING_METRICS_COLON
    : COLON -> type(COLON), popMode, pushMode(METRICS_MODE)
    ;

CLOSING_METRICS_COMMA
    : COMMA -> type(COMMA), popMode, pushMode(METRICS_MODE)
    ;

CLOSING_METRICS_LINE_COMMENT
    : LINE_COMMENT -> channel(HIDDEN)
    ;

CLOSING_METRICS_MULTILINE_COMMENT
    : MULTILINE_COMMENT -> channel(HIDDEN)
    ;

CLOSING_METRICS_WS
    : WS -> channel(HIDDEN)
    ;

CLOSING_METRICS_QUOTED_IDENTIFIER
    : QUOTED_IDENTIFIER -> popMode, pushMode(EXPRESSION_MODE), type(QUOTED_IDENTIFIER)
    ;

CLOSING_METRICS_UNQUOTED_IDENTIFIER
    :UNQUOTED_IDENTIFIER -> popMode, pushMode(EXPRESSION_MODE), type(UNQUOTED_IDENTIFIER)
    ;

CLOSING_METRICS_BY
    :BY -> popMode, pushMode(EXPRESSION_MODE), type(BY)
    ;

CLOSING_METRICS_PIPE
    : PIPE -> type(PIPE), popMode
    ;
