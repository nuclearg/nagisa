package com.github.nuclearg.nagisa.lang.ast;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * return语句
 * 
 * @author ng
 *
 */
public final class ReturnStmt extends Stmt {
    /**
     * 返回值的表达式
     */
    private final Expr expr;

    ReturnStmt(SyntaxTreeNode node, Context ctx) {
        if (node.getChildren().size() > 2)
            this.expr = Expr.resolveExpr(node.getChildren().get(1), ctx);
        else
            this.expr = null;
    }

    /** 返回值的表达式 */
    public Expr getExpr() {
        return this.expr;
    }

    @Override
    public String toString(String prefix) {
        if (this.expr == null)
            return prefix + "RETURN" + SystemUtils.LINE_SEPARATOR;
        else
            return prefix + " RETURN " + this.expr + SystemUtils.LINE_SEPARATOR;
    }
}
