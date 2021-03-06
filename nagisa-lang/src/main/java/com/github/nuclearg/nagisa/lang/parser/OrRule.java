package com.github.nuclearg.nagisa.lang.parser;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.lexer.LexToken;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;

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
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        // 需要根据下一个词法元素类型来选择使用哪一条规则
        LexToken token = lexer.peek();

        for (SyntaxRule rule : this.rules)
            if (rule.tryToken(token.getType()))
                return rule.parse(lexer, errorReporter);

        // 错误处理
        errorReporter.error("找不到合适的语法规则，遇到意外的符号 " + token, lexer.snapshot());
        return null;
    }

    @Override
    boolean tryToken(LexTokenType tokenType) {
        for (SyntaxRule rule : this.rules)
            if (rule.tryToken(tokenType))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return StringUtils.join(this.rules.stream().map(r -> "(" + r + ")").toArray(), "|");
    }
}
