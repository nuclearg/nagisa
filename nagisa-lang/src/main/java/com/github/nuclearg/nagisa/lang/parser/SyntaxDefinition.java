package com.github.nuclearg.nagisa.lang.parser;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;

/**
 * 语法定义
 * 
 * @author ng
 *
 */
public abstract class SyntaxDefinition {
    /**
     * 语法规则列表
     */
    private final Map<String, SyntaxRule> rules = new LinkedHashMap<>();

    /** 语法规则列表 */
    public SyntaxRule getRule(String name) {
        return this.rules.get(name);
    }

    protected final void define(String name, SyntaxRule rule) {
        this.rules.put(name, rule);
    }

    protected final SyntaxRule seq(SyntaxRule... elements) {
        return new SequenceRule(Arrays.asList(elements));
    }

    protected final SyntaxRule lex(LexTokenType tokenType) {
        return new LexRule(tokenType);
    }

    protected final SyntaxRule nul() {
        return new NullRule();
    }

    protected final SyntaxRule ref(String name) {
        return new RefRule(this, name);
    }

    protected final SyntaxRule or(SyntaxRule... rules) {
        return new OrRule(Arrays.asList(rules));
    }

    protected final SyntaxRule rep(SyntaxRule rule) {
        return new RepeatRule(rule);
    }
}
