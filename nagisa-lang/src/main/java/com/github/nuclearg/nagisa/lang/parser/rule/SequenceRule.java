package com.github.nuclearg.nagisa.lang.parser.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerSnapshot;
import com.github.nuclearg.nagisa.lang.parser.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 表示或的语法关系
 * 
 * @author ng
 *
 */
public class SequenceRule implements SyntaxRule {
    private final List<SyntaxRule> rules;

    public SequenceRule(List<SyntaxRule> elements) {
        this.rules = Collections.unmodifiableList(elements);
    }

    @Override
    public boolean tryToken(LexTokenType tokenType) {
        return this.rules.get(0).tryToken(tokenType);
    }

    @Override
    public SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        List<SyntaxTreeNode> children = new ArrayList<>();

        LexTokenizerSnapshot snapshot = lexer.snapshot();

        for (SyntaxRule rule : this.rules) {
            // 尝试解析下一个元素
            SyntaxTreeNode node = rule.parse(lexer, errorReporter);

            if (node == null) {
                errorReporter.error("Syntax error. expected:  " + rule + ", but: " + lexer.next(), lexer, this);
                lexer.restore(snapshot);
                return null;
            }
            children.add(node);
        }

        return new SyntaxTreeNode(this, children);
    }

    @Override
    public String toString() {
        return StringUtils.join(this.rules, " ");
    }

}
