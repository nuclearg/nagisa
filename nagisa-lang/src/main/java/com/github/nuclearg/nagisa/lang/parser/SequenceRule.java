package com.github.nuclearg.nagisa.lang.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerSnapshot;

/**
 * 表示或的语法关系
 * 
 * @author ng
 *
 */
final class SequenceRule extends SyntaxRule {
    private final List<SyntaxRule> rules;

    SequenceRule(List<SyntaxRule> elements) {
        this.rules = Collections.unmodifiableList(elements);
    }

    @Override
    boolean tryToken(LexTokenType tokenType) {
        return this.rules.get(0).tryToken(tokenType);
    }

    @Override
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        List<SyntaxTreeNode> children = new ArrayList<>();

        for (SyntaxRule rule : this.rules) {
            LexTokenizerSnapshot snapshot = lexer.snapshot();

            // 尝试解析下一个元素
            SyntaxTreeNode node = rule.parse(lexer, errorReporter);

            // 正常情况，语法树节点成功构建
            if (node != null) {
                if (node.getRange() == null) // 这表示是一个空节点
                    continue;

                /*
                 * 把子节点挂到children里
                 */
                if (node.getToken() != null || node.getRuleName() != null)
                    // 如果rule是ref或lex，则把节点挂到本节点的children里
                    children.add(node);
                else
                    // 其它情况则忽略掉这一层节点，把里面的children直接提升成本节点的children
                    children.addAll(node.getChildren());
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
