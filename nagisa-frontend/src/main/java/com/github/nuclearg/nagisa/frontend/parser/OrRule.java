package com.github.nuclearg.nagisa.frontend.parser;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.lexer.LexToken;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenizer;

/**
 * 表示或的语法关系
 * 
 * @author ng
 *
 */
final class OrRule extends SyntaxRule {
    private final List<SyntaxRule> rules;

    OrRule(List<SyntaxRule> rules) {
        this.rules = Collections.unmodifiableList(rules);
    }

    @Override
    boolean tryToken(LexTokenType tokenType, SyntaxErrorReporter errorReporter) {
        for (SyntaxRule rule : this.rules)
            if (rule.tryToken(tokenType, errorReporter))
                return true;
        return false;
    }

    @Override
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        // 需要根据下一个词法元素类型来选择使用哪一条规则
        LexToken token = lexer.peek();
        for (SyntaxRule rule : this.rules)
            if (rule.tryToken(token.getType(), errorReporter))
                return rule.parse(lexer, errorReporter);

        // 没有规则支持当前的符号，报错返回
        errorReporter.report(lexer.position(), Errors.E0002, token);
        return null;
    }

    @Override
    public String toString() {
        return StringUtils.join(this.rules.stream().map(r -> "(" + r + ")").toArray(), "|");
    }
}
