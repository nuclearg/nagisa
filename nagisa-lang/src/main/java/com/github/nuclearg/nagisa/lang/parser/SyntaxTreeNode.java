package com.github.nuclearg.nagisa.lang.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.lexer.LexToken;
import com.github.nuclearg.nagisa.lang.parser.rule.NullRule;
import com.github.nuclearg.nagisa.lang.parser.rule.SyntaxRule;
import com.github.nuclearg.nagisa.lang.util.ListUtils;
import com.github.nuclearg.nagisa.lang.util.Range;

/**
 * 语法树节点
 * 
 * @author ng
 *
 */
public class SyntaxTreeNode {
    /**
     * 对应的语法元素
     */
    public final SyntaxRule rule;
    /**
     * 对应的语法元素的名称
     */
    public final String ruleName;
    /**
     * 对应的词法元素
     */
    public final List<LexToken> tokens;
    /**
     * 子元素，可能为null
     */
    public final List<SyntaxTreeNode> children;
    /**
     * 在源文件中的位置
     */
    public final Range range;

    public SyntaxTreeNode(SyntaxRule rule, LexToken token) {
        this.rule = rule;
        this.ruleName = token.type.name();
        this.tokens = Arrays.asList(token);
        this.children = null;
        this.range = token.range;
    }

    public SyntaxTreeNode(SyntaxRule rule, List<SyntaxTreeNode> children) {
        this.rule = rule;
        this.ruleName = null;
        this.tokens = children.stream().map(e -> e.tokens).reduce(Collections.emptyList(), ListUtils::union);
        this.children = Collections.unmodifiableList(children);

        if (!this.children.isEmpty()) {
            SyntaxTreeNode first = children.get(0);
            SyntaxTreeNode last = children.get(children.size() - 1);
            this.range = new Range(first.range.startRow, first.range.startColumn, last.range.endRow, last.range.endColumn);
        } else
            this.range = new Range(0, 0, 0, 0);
    }

    public SyntaxTreeNode(NullRule rule, Range range) {
        this.rule = rule;
        this.ruleName = null;
        this.tokens = Collections.emptyList();
        this.children = null;
        this.range = range;
    }

    public SyntaxTreeNode(String name, SyntaxTreeNode node) {
        this.rule = node.rule;
        this.ruleName = name;
        this.tokens = node.tokens;
        this.children = node.children;
        this.range = node.range;
    }

    @Override
    public String toString() {
        return StringUtils.join(this.tokens.stream().map(e -> e.text).toArray(), " ");
    }
}
