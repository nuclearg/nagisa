package com.github.nuclearg.nagisa.compiler.parser;

import java.util.List;

import com.github.nuclearg.nagisa.util.Range;

/**
 * 语法树节点
 * 
 * @author ng
 *
 */
public class SyntaxTreeNode {
    /**
     * 对应的文本
     */
    public final String text;
    /**
     * 子元素
     */
    public final List<SyntaxTreeNode> children;
    /**
     * 在源文件中的位置
     */
    public final Range range;

    public SyntaxTreeNode(String text, List<SyntaxTreeNode> children, Range range) {
        this.text = text;
        this.children = children;
        this.range = range;
    }

}
