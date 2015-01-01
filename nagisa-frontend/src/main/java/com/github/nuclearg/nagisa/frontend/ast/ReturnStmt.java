package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

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
            this.expr = Expr.buildExpr(node.getChildren().get(1), ctx);
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
            return prefix + "RETURN" + LN;
        else
            return prefix + " RETURN " + this.expr + LN;
    }
}
