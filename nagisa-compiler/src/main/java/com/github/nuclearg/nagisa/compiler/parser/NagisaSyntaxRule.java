package com.github.nuclearg.nagisa.compiler.parser;

/**
 * nagisa的语法定义
 * 
 * @author ng
 *
 */
public enum NagisaSyntaxRule implements SyntaxRule {
    expr(""),
    
    ;
    private final String[] rule;

    private NagisaSyntaxRule(String... rule) {
        this.rule = rule;
    }

    @Override
    public String[] rule() {
        return this.rule;
    }

}
