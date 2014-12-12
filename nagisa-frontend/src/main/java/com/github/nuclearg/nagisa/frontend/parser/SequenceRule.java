package com.github.nuclearg.nagisa.frontend.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.lexer.LexToken;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition;
import com.github.nuclearg.nagisa.frontend.util.Position;

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
    boolean tryToken(LexTokenType tokenType, SyntaxErrorReporter errorReporter) {
        return this.rules.get(0).tryToken(tokenType, errorReporter);
    }

    @Override
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        List<SyntaxTreeNode> children = new ArrayList<>();

        for (SyntaxRule rule : this.rules) {
            // 尝试解析下一个元素
            SyntaxTreeNode node = this.tryParse(lexer, rule, errorReporter);

            // 如果解析失败则报错返回
            if (node == null)
                return null;

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

    /**
     * 尝试解析语法规则，如果当前词解析失败则尝试跳过当前的词，直到解析成功或遇到EOL或EOF
     * 
     * @param lexer
     *            词法解析器
     * @param rule
     *            语法规则
     * @param errorReporter
     *            错误报告喝咖啡
     * @return 解析出来的语法树，或为null，为null时会将lexer恢复到原始状态
     */
    private final SyntaxTreeNode tryParse(LexTokenizer lexer, SyntaxRule rule, SyntaxErrorReporter errorReporter) {
        Position position = lexer.position();

        SyntaxTreeNode node;

        while ((node = rule.parse(lexer, errorReporter)) == null) {
            // 跳过当前token
            LexToken token = lexer.next();

            // 如果当前token是EOF或EOL则报告解析失败
            if (token.getType() == null/* EOF */|| token.getType() == NagisaLexDefinition.NagisaLexTokenType.EOL /* EOL */) {
                // 恢复lexer，返回解析失败
                lexer.restore(position);
                return null;
            }
        }

        return node;
    }
}
