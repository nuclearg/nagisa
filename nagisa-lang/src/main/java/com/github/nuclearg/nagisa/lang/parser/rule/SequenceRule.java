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

        for (SyntaxRule rule : this.rules) {
            LexTokenizerSnapshot snapshot = lexer.snapshot();

            // 尝试解析下一个元素
            SyntaxTreeNode node = rule.parse(lexer, errorReporter);

            // 正常情况，语法树节点成功构建
            if (node != null) {
                children.add(node);
            } else {
                // 错误处理，跳过当前的词向后看
                errorReporter.error("语法子树构建失败。当前符号：" + lexer.peek() + ". 期望 " + rule, snapshot);
            }
        }

        return new SyntaxTreeNode(this, children);
    }

    @Override
    public String toString() {
        return StringUtils.join(this.rules, " ");
    }

}
