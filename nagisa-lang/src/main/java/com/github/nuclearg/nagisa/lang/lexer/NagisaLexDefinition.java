package com.github.nuclearg.nagisa.lang.lexer;

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
     * @return 词法解析器
     */
    public static LexTokenizer lexer(String text) {
        return new LexTokenizer(INSTANCE, text);
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
        REMARK(Pattern.compile("//.*$"), true),

        /**
         * LET
         */
        KEYWORD_LET("LET"),
        /**
         * IF
         */
        KEYWORD_IF("IF"),
        /**
         * THEN
         */
        KEYWORD_THEN("THEN"),
        /**
         * ELSE
         */
        KEYWORD_ELSE("ELSE"),

        /**
         * FOR
         */
        KEYWORD_FOR("FOR"),
        /**
         * TO
         */
        KEYWORD_TO("TO"),
        /**
         * NEXT
         */
        KEYWORD_NEXT("NEXT"),
        /**
         * WHILE
         */
        KEYWORD_WHILE("WHILE"),
        /**
         * BREAK
         */
        KEYWORD_BREAK("BREAK"),
        /**
         * CONTINUE
         */
        KEYWORD_CONEINUE("CONTINUE"),
        /**
         * END
         */
        KEYWORD_END("END"),
        /**
         * FUNCTION
         */
        KEYWORD_FUNCTION("FUNCTION"),
        /**
         * SUB
         */
        KEYWORD_SUB("SUB"),
        /**
         * RETURN
         */
        KEYWORD_RETURN("RETURN"),
        /**
         * GOTO
         */
        KEYWORD_GOTO("GOTO"),

        /**
         * 字符串变量名
         */
        IDENTIFIER_STRING(Pattern.compile("[a-zA-Z][_0-9a-zA-Z]*\\$")),
        /**
         * 用户自定义的符号
         */
        IDENTIFIER_INTEGER(Pattern.compile("[a-zA-Z][_0-9a-zA-Z]*")),
        /**
         * 整数
         */
        LITERAL_INTEGER(Pattern.compile("[0-9]+")),
        /**
         * 字符串
         */
        LITERAL_STRING(Pattern.compile("\"([^\"]|\\\\\")+\"")),

        /**
         * 加号
         */
        SYMBOL_ADD("+"),
        /**
         * 减号
         */
        SYMBOL_SUB("-"),
        /**
         * 乘号
         */
        SYMBOL_MUL("*"),
        /**
         * 除号
         */
        SYMBOL_DIV("/"),
        /**
         * 求余
         */
        SYMBOL_MOD("%"),
        /**
         * 等于
         */
        SYMBOL_EQ("=="),
        /**
         * 不等于
         */
        SYMBOL_NEQ("!="),
        /**
         * 大于
         */
        SYMBOL_GT(">"),
        /**
         * 大于等于
         */
        SYMBOL_GTE(">="),
        /**
         * 小于
         */
        SYMBOL_LT("<"),
        /**
         * 小于等于
         */
        SYMBOL_LTE("<="),
        /**
         * 逻辑且
         */
        SYMBOL_AND("&&"),
        /**
         * 逻辑或
         */
        SYMBOL_OR("||"),
        /**
         * 逻辑异或
         */
        SYMBOL_XOR("^"),
        /**
         * 逻辑与
         */
        SYMBOL_NOT("!"),
        /**
         * 赋值运算符
         */
        SYMBOL_LET("="),

        /**
         * 左小括号
         */
        SYMBOL_PARENTHESE_LEFT("("),
        /**
         * 右小括号
         */
        SYMBOL_PARENTHESE_RIGHT(")"),
        /**
         * 左中括号
         */
        SYMBOL_BRACKET_LEFT("["),
        /**
         * 右中括号
         */
        SYMBOL_BRACKET_RIGHT("]"),

        /**
         * 逗号
         */
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
