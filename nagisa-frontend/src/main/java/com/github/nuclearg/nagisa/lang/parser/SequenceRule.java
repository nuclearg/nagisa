package com.github.nuclearg.nagisa.lang.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.Position;

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

        Position position = lexer.position();
        for (SyntaxRule rule : this.rules) {
            // 尝试解析下一个元素
            SyntaxTreeNode node = this.tryParse(lexer, rule, errorReporter);

            // 如果解析失败则报错返回
            if (node == null) {
                lexer.restore(position);
                return null;
            }

            // 正常情况，语法树节点成功构建
            if (node.getRange() == null) // 这表示是一个空节点，忽略
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
        }

        return new SyntaxTreeNode(this, children);
    }

    @Override
    public String toString() {
        return StringUtils.join(this.rules, " ");
    }

}
