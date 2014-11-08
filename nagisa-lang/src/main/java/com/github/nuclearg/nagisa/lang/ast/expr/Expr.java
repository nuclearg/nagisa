package com.github.nuclearg.nagisa.lang.ast.expr;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.ast.AstNode;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 表达式
 * 
 * @author ng
 *
 */
public final class Expr extends AstNode {
    /**
     * 表达式类型
     */
    private final ExprType type;
    /**
     * 表达式的字面量
     */
    private final String text;
    /**
     * 该表达式的各个子表达式
     */
    private final List<Expr> children;

    private Expr(ExprType type, String text) {
        this.type = type;
        this.text = text;
        this.children = null;
    }

    private Expr(ExprType type, List<Expr> children) {
        this.type = type;
        this.text = null;
        this.children = Collections.unmodifiableList(children);
    }

    /** 表达式类型 */
    public ExprType getType() {
        return this.type;
    }

    /** 表达式的字面量 */
    public String getText() {
        return this.text;
    }

    /** 该表达式的各个子表达式 */
    public List<Expr> getChildren() {
        return this.children;
    }

    @Override
    public String toString() {
        if (this.text != null)
            return this.text;
        if (this.children != null)
            return StringUtils.join(this.children, " ");
        return "";
    }

    public static Expr resolveExpr(SyntaxTreeNode node) {
        return null;
    }

}
