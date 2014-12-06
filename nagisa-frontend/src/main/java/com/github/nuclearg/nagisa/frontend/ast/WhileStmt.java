package com.github.nuclearg.nagisa.frontend.ast;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * while循环语句
 * 
 * @author ng
 *
 */
public final class WhileStmt extends Stmt {
    /**
     * 循环判断条件
     */
    private final Expr condition;
    /**
     * 循环体
     */
    private final List<Stmt> stmts;

    WhileStmt(SyntaxTreeNode node, Context ctx) {
        this.condition = Expr.resolveExpr(node.getChildren().get(1), ctx);
        this.stmts = Stmt.resolveStmts(node.getChildren().get(3).getChildren(), ctx);
    }

    /** 循环判断条件 */
    public Expr getCondition() {
        return this.condition;
    }

    /** 循环体 */
    public Iterable<Stmt> getStmts() {
        return this.stmts;
    }

    @Override
    public String toString(String prefix) {
        return prefix + "WHILE " + this.condition + SystemUtils.LINE_SEPARATOR
                + Stmt.toString(this.stmts, prefix + "    ")
                + prefix + "END WHILE" + SystemUtils.LINE_SEPARATOR;
    }
}
