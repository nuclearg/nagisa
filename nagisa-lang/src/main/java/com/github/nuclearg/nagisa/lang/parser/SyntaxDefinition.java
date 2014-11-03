package com.github.nuclearg.nagisa.lang.parser;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.parser.rule.LexRule;
import com.github.nuclearg.nagisa.lang.parser.rule.NullRule;
import com.github.nuclearg.nagisa.lang.parser.rule.OrRule;
import com.github.nuclearg.nagisa.lang.parser.rule.RefRule;
import com.github.nuclearg.nagisa.lang.parser.rule.RepeatRule;
import com.github.nuclearg.nagisa.lang.parser.rule.SequenceRule;
import com.github.nuclearg.nagisa.lang.parser.rule.SyntaxRule;

/**
 * 语法定义
 * 
 * @author ng
 *
 */
public class SyntaxDefinition {
    /**
     * 语法规则列表
     */
    public final Map<String, SyntaxRule> rules = new LinkedHashMap<>();

    protected void define(String name, SyntaxRule rule) {
        this.rules.put(name, rule);
    }

    protected SyntaxRule seq(SyntaxRule... elements) {
        return new SequenceRule(Arrays.asList(elements));
    }

    protected SyntaxRule lex(LexTokenType tokenType) {
        return new LexRule(tokenType);
    }

    protected SyntaxRule nul() {
        return new NullRule();
    }

    protected SyntaxRule ref(String name) {
        return new RefRule(this, name);
    }

    protected SyntaxRule or(SyntaxRule... rules) {
        return new OrRule(Arrays.asList(rules));
    }

    protected SyntaxRule rep(SyntaxRule rule) {
        return new RepeatRule(rule);
    }
}
