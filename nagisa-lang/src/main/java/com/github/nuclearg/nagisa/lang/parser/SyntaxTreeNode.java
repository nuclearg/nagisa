package com.github.nuclearg.nagisa.lang.parser;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.lexer.LexToken;
import com.github.nuclearg.nagisa.lang.util.Range;

/**
 * 语法树节点
 * 
 * @author ng
 *
 */
public final class SyntaxTreeNode {
    /**
     * 对应的语法规则
     */
    private final SyntaxRule rule;
    /**
     * 对应的语法规则的名称
     */
    private final String ruleName;
    /**
     * 对应的词法元素，可能为null
     */
    private final LexToken token;
    /**
     * 子元素列表
     */
    private final List<SyntaxTreeNode> children;
    /**
     * 在源文件中的位置
     */
    private final Range range;

    SyntaxTreeNode(SyntaxRule rule, LexToken token) {
        this.rule = rule;
        this.ruleName = null;
        this.token = token;
        this.children = Collections.emptyList();
        this.range = token.getRange();
    }

    SyntaxTreeNode(SyntaxRule rule, List<SyntaxTreeNode> children) {
        this.rule = rule;
        this.ruleName = null;
        this.token = null;
        this.children = Collections.unmodifiableList(children);

        if (!this.children.isEmpty()) {
            SyntaxTreeNode first = children.get(0);
            SyntaxTreeNode last = children.get(children.size() - 1);
            this.range = new Range(first.range.startRow, first.range.startColumn, last.range.endRow, last.range.endColumn);
        } else
            this.range = new Range(0, 0, 0, 0);
    }

    SyntaxTreeNode(NullRule rule) {
        this.rule = rule;
        this.ruleName = null;
        this.token = null;
        this.children = null;
        this.range = null;
    }

    SyntaxTreeNode(String name, SyntaxTreeNode node) {
        this.rule = node.rule;
        this.ruleName = name;
        this.token = node.token;
        this.children = node.children;
        this.range = node.range;
    }

    /** 对应的语法规则 */
    public SyntaxRule getRule() {
        return this.rule;
    }

    /** 对应的语法规则的名称 */
    public String getRuleName() {
        return this.ruleName;
    }

    /** 对应的词法元素，可能为null */
    public LexToken getToken() {
        return this.token;
    }

    /** 子元素列表，可能为null */
    public List<SyntaxTreeNode> getChildren() {
        return this.children;
    }

    /** 在源文件中的位置 */
    public Range getRange() {
        return this.range;
    }

    @Override
    public String toString() {
        if (this.token != null)
            return this.token.toString();
        if (this.children != null)
            return "<" + StringUtils.join(this.children, " ") + ">";
        return "<>";
    }
}
