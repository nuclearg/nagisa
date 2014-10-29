package com.github.nuclearg.nagisa.compiler.lex;

import java.util.regex.Pattern;

/**
 * nagisa的词法定义
 * 
 * @author ng
 *
 */
public enum NagisaLexDefinition implements LexTokenType {
    /**
     * 空白
     */
    SPACE("( |\\t)+", true),
    /**
     * 换行
     */
    LN("[\\r\\n]+"),
    /**
     * 注释
     */
    REMARK("//.*$", true),

    /**
     * IF
     */
    KEYWORD_IF("(?i)IF"),
    /**
     * THEN
     */
    KEYWORD_THEN("(?i)THEN"),
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
     * 用户自定义的符号
     */
    SYMBOL("[_0-9a-zA-Z]"),
    /**
     * 整数
     */
    INTEGER("[0-9]+"),
    /**
     * 字符串
     */
    STRING("\"([^\"]|\\\\\")+\""),

    /**
     * 赋值
     */
    OPERATOR_LET("="),
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
    OPERATOR_BRACKET_RIGHT("\\]"), ;

    private final Pattern regex;
    private final boolean transparent;

    private NagisaLexDefinition(String regex) {
        this(regex, false);
    }

    private NagisaLexDefinition(String regex, boolean transparent) {
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
