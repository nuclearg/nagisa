package com.github.nuclearg.nagisa.frontend.lexer;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * nagisa的词法定义
 * 
 * @author ng
 *
 */
public final class NagisaLexDefinition extends LexDefinition {
    private static final NagisaLexDefinition INSTANCE = new NagisaLexDefinition();

    private NagisaLexDefinition() {
        super(Arrays.asList(NagisaLexTokenType.values()));
    }

    /**
     * 构造一个词法解析器
     * 
     * @param text
     *            被解析的文本
     * @param 文件名
     * @return 词法解析器
     */
    public static LexTokenizer lexer(String text, String fileName) {
        return new LexTokenizer(INSTANCE, text, fileName);
    }

    public static enum NagisaLexTokenType implements LexTokenType {
        /**
         * 空白
         */
        SPACE(Pattern.compile("( |\\t)+"), true),
        /**
         * 换行
         */
        EOL(Pattern.compile("[\\r\\n]+")),
        /**
         * 注释
         */
        REMARK(Pattern.compile("\\/\\/.*$"), true),

        KEYWORD_GOTO("GOTO"),
        KEYWORD_END("END"),

        KEYWORD_DIM("DIM"),
        KEYWORD_AS("AS"),
        KEYWORD_LET("LET"),

        KEYWORD_IF("IF"),
        KEYWORD_THEN("THEN"),
        KEYWORD_ELSE("ELSE"),

        KEYWORD_FOR("FOR"),
        KEYWORD_TO("TO"),
        KEYWORD_NEXT("NEXT"),
        KEYWORD_WHILE("WHILE"),

        KEYWORD_FUNCTION("FUNCTION"),
        KEYWORD_SUB("SUB"),
        KEYWORD_NATIVEFUNCTION("NATIVEFUNCTION"),
        KEYWORD_NATIVESUB("NATIVESUB"),
        KEYWORD_RETURN("RETURN"),

        /**
         * 符号名
         */
        IDENTIFIER(Pattern.compile("[a-zA-Z][_0-9a-zA-Z]*")),
        /**
         * 整数字面量
         */
        LITERAL_INTEGER(Pattern.compile("[0-9]+")),
        /**
         * 字符串字面量
         */
        LITERAL_STRING(Pattern.compile("\"([^\"]|\\\\\")+\"")),

        SYMBOL_ADD("+"),
        SYMBOL_SUB("-"),
        SYMBOL_MUL("*"),
        SYMBOL_DIV("/"),
        SYMBOL_MOD("%"),
        SYMBOL_EQ("=="),
        SYMBOL_NEQ("!="),
        SYMBOL_GT(">"),
        SYMBOL_GTE(">="),
        SYMBOL_LT("<"),
        SYMBOL_LTE("<="),
        SYMBOL_AND("&&"),
        SYMBOL_OR("||"),
        SYMBOL_NOT("!"),
        SYMBOL_LET("="),

        SYMBOL_PARENTHESE_LEFT("("),
        SYMBOL_PARENTHESE_RIGHT(")"),
        SYMBOL_BRACKET_LEFT("["),
        SYMBOL_BRACKET_RIGHT("]"),

        SYMBOL_COMMA(","),

        ;

        private final String literal;
        private final Pattern regex;
        private final boolean transparent;

        private NagisaLexTokenType(String literal) {
            this.literal = literal;
            this.regex = null;
            this.transparent = false;
        }

        private NagisaLexTokenType(Pattern regex) {
            this(regex, false);
        }

        private NagisaLexTokenType(Pattern regex, boolean transparent) {
            this.literal = null;
            this.regex = regex;
            this.transparent = transparent;
        }

        @Override
        public String literal() {
            return this.literal;
        }

        @Override
        public Pattern regex() {
            return this.regex;
        }

        @Override
        public boolean transparent() {
            return this.transparent;
        }
    }
}
