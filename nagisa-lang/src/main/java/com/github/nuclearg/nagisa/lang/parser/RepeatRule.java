package com.github.nuclearg.nagisa.lang.parser;

import java.util.ArrayList;
import java.util.List;

import com.github.nuclearg.nagisa.lang.lexer.LexToken;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;

/**
 * 可能存在零次或多次的语法规则
 * 
 * @author ng
 *
 */
final class RepeatRule extends SyntaxRule {
    /**
     * 可选的规则
     */
    private final SyntaxRule rule;

    RepeatRule(SyntaxRule rule) {
        this.rule = rule;
    }

    @Override
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        List<SyntaxTreeNode> children = new ArrayList<>();

        while (true) {
            // 获取下一个词
            LexToken token = lexer.peek();

            // 判断这个词是否可以被规则接收
            if (this.rule.tryToken(token.getType()))
                children.add(this.rule.parse(lexer, errorReporter));
            else
                break;
        }

        return new SyntaxTreeNode(this, children);
    }

    @Override
    boolean tryToken(LexTokenType tokenType) {
        return true;
    }

    @Override
    public String toString() {
        return "{" + this.rule + "}";
    }
}
