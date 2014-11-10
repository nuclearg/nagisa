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
        SPACE("( |\\t)+", true),
        /**
         * 换行
         */
        EOL("[\\r\\n]+"),
        /**
         * 注释
         */
        REMARK("//.*$", true),

        /**
         * LET
         */
        KEYWORD_LET("(?i)LET"),
        /**
         * IF
         */
        KEYWORD_IF("(?i)IF"),
        /**
         * THEN
         */
        KEYWORD_THEN("(?i)THEN"),
        /**
         * ELSE
         */
        KEYWORD_ELSE("(?i)ELSE"),
        /**
         * ENDIF
         */
        KEYWORD_ENDIF("(?i)ENDIF"),

        /**
         * FOR
         */
        KEYWORD_FOR("(?i)FOR"),
        /**
         * TO
         */
        KEYWORD_TO("(?i)TO"),
        /**
         * NEXT
         */
        KEYWORD_NEXT("(?i)NEXT"),
        /**
         * WHILE
         */
        KEYWORD_WHILE("(?i)WHILE"),
        /**
         * WEND
         */
        KEYWORD_WEND("(?i)WEND"),

        /**
         * 字符串变量名
         */
        STRING_SYMBOL("[a-zA-Z][_0-9a-zA-Z]*\\$"),
        /**
         * 用户自定义的符号
         */
        SYMBOL("[a-zA-Z][_0-9a-zA-Z]*"),
        /**
         * 整数
         */
        INTEGER("[0-9]+"),
        /**
         * 字符串
         */
        STRING("\"([^\"]|\\\\\")+\""),

        /**
         * 加号
         */
        OPERATOR_ADD("\\+"),
        /**
         * 减号
         */
        OPERATOR_SUB("-"),
        /**
         * 乘号
         */
        OPERATOR_MUL("\\*"),
        /**
         * 除号
         */
        OPERATOR_DIV("/"),
        /**
         * 求余
         */
        OPERATOR_MOD("%"),
        /**
         * 等于
         */
        OPERATOR_EQ("=="),
        /**
         * 不等于
         */
        OPERATOR_NEQ("!="),
        /**
         * 大于
         */
        OPERATOR_GT(">"),
        /**
         * 大于等于
         */
        OPERATOR_GTE(">="),
        /**
         * 小于
         */
        OPERATOR_LT("<"),
        /**
         * 小于等于
         */
        OPERATOR_LTE("<="),
        /**
         * 逻辑且
         */
        OPERATOR_AND("&&"),
        /**
         * 逻辑或
         */
        OPERATOR_OR("\\|\\|"),
        /**
         * 逻辑异或
         */
        OPERATOR_XOR("\\^"),
        /**
         * 逻辑与
         */
        OPERATOR_NOT("!"),
        /**
         * 左小括号
         */
        OPERATOR_PARENTHESE_LEFT("\\("),
        /**
         * 右小括号
         */
        OPERATOR_PARENTHESE_RIGHT("\\)"),
        /**
         * 左中括号
         */
        OPERATOR_BRACKET_LEFT("\\["),
        /**
         * 右中括号
         */
        OPERATOR_BRACKET_RIGHT("\\]"),

        /**
         * 赋值
         */
        OPERATOR_LET("="), ;

        private final Pattern regex;
        private final boolean transparent;

        private NagisaLexTokenType(String regex) {
            this(regex, false);
        }

        private NagisaLexTokenType(String regex, boolean transparent) {
            this.regex = Pattern.compile(regex);
            this.transparent = transparent;
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
