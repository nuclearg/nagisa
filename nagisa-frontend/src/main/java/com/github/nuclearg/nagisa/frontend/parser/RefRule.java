package com.github.nuclearg.nagisa.frontend.parser;

import com.github.nuclearg.nagisa.frontend.error.Fatals;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenizer;

/**
 * 引用其它{@link SyntaxRuleDefinition}
 * 
 * @author ng
 *
 */
final class RefRule extends SyntaxRule {
    /**
     * 引用的语法定义
     */
    private final SyntaxDefinition definition;
    /**
     * 引用的语法规则定义式的名称
     */
    private final String name;

    RefRule(SyntaxDefinition definition, String name) {
        this.definition = definition;
        this.name = name;
    }

    @Override
    boolean tryToken(LexTokenType tokenType, SyntaxErrorReporter errorReporter) {
        return this.rule(errorReporter).tryToken(tokenType, errorReporter);
    }

    @Override
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        SyntaxTreeNode node = this.rule(errorReporter).parse(lexer, errorReporter);

        if (node == null)
            return null;

        if (node.getRuleName() != null)
            return node;
        else
            return new SyntaxTreeNode(this.name, node);
    }

    private SyntaxRule rule(SyntaxErrorReporter errorReporter) {
        SyntaxRule rule = this.definition.getRule(this.name);
        if (rule == null)
            errorReporter.report(Fatals.F0001, "syntax rule not exists. ruleName: " + this.name);
        return rule;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
