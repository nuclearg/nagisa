package com.github.nuclearg.nagisa.lang.parser.rule;

import java.util.ArrayList;
import java.util.List;

import com.github.nuclearg.nagisa.lang.lexer.LexToken;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.parser.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 可能存在零次或多次的语法规则
 * 
 * @author ng
 *
 */
public class RepeatRule implements SyntaxRule {
    /**
     * 可选的规则
     */
    private final SyntaxRule rule;

    public RepeatRule(SyntaxRule rule) {
        this.rule = rule;
    }

    @Override
    public SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        List<SyntaxTreeNode> children = new ArrayList<>();

        while (true) {
            // 获取下一个词
            LexToken token = lexer.peek();

            // 判断这个词是否可以被规则接收
            if (this.rule.tryToken(token.type))
                children.add(this.rule.parse(lexer, errorReporter));
            else
                break;
        }

        return new SyntaxTreeNode(this, children);
    }

    @Override
    public boolean tryToken(LexTokenType tokenType) {
        return true;
    }

    @Override
    public String toString() {
        return "{" + this.rule + "}";
    }
}
