package com.github.nuclearg.nagisa.compiler.parser;

import java.util.List;

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
    public final List<SyntaxRule> rules;
    /**
     * 作为树根的语法规则
     */
    public final SyntaxRule rootRule;

    public SyntaxDefinition(List<SyntaxRule> rules, SyntaxRule rootRule) {
        this.rules = rules;
        this.rootRule = rootRule;
    }

}
