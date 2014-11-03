package com.github.nuclearg.nagisa.lang.parser.rule;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.parser.SyntaxDefinition;
import com.github.nuclearg.nagisa.lang.parser.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 引用其它{@link SyntaxRuleDefinition}
 * 
 * @author ng
 *
 */
public class RefRule implements SyntaxRule {
    /**
     * 引用的语法定义
     */
    private final SyntaxDefinition definition;
    /**
     * 引用的语法规则定义式的名称
     */
    private final String name;

    public RefRule(SyntaxDefinition definition, String name) {
        this.definition = definition;
        this.name = name;
    }

    private SyntaxRule rule() {
        return this.definition.rules.get(this.name);
    }

    @Override
    public boolean tryToken(LexTokenType tokenType) {
        return this.rule().tryToken(tokenType);
    }

    @Override
    public SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        SyntaxTreeNode node = this.rule().parse(lexer, errorReporter);
        if (node.ruleName != null)
            return node;
        else
            return new SyntaxTreeNode(this.name, node);
    }

    @Override
    public String toString() {
        return this.name;
    }

}
