package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 判断语句
 * 
 * @author ng
 *
 */
public final class IfStmt extends Stmt {
    /**
     * 条件表达式
     */
    private final Expr condition;
    /**
     * 判断成功的操作
     */
    private final List<Stmt> thenStmts;
    /**
     * 判断失败的操作
     */
    private final List<Stmt> elseStmts;

    IfStmt(SyntaxTreeNode node, Context ctx) {
        this.condition = Expr.resolveExpr(node.getChildren().get(1), ctx);

        this.thenStmts = Stmt.resolveStmts(node.getChildren().get(4).getChildren(), ctx);

        if (!node.getChildren().get(7).getChildren().isEmpty())
            this.elseStmts = Stmt.resolveStmts(node.getChildren().get(7).getChildren(), ctx);
        else
            this.elseStmts = Collections.emptyList();
    }

    /** 条件表达式 */
    public Expr getCondition() {
        return this.condition;
    }

    /** 判断成功的操作 */
    public Iterable<Stmt> getThenStmts() {
        return this.thenStmts;
    }

    /** 判断失败的操作 */
    public Iterable<Stmt> getElseStmts() {
        return this.elseStmts;
    }

    @Override
    public String toString(String prefix) {
        if (this.elseStmts.isEmpty())
            return prefix + "IF " + this.condition + " THEN" + SystemUtils.LINE_SEPARATOR
                    + Stmt.toString(this.thenStmts, prefix + "    ")
                    + prefix + "END IF" + SystemUtils.LINE_SEPARATOR;
        else
            return prefix + "IF " + this.condition + " THEN" + SystemUtils.LINE_SEPARATOR
                    + Stmt.toString(this.thenStmts, prefix + "    ")
                    + prefix + "ELSE" + SystemUtils.LINE_SEPARATOR
                    + Stmt.toString(this.elseStmts, prefix + "    ")
                    + prefix + "END IF" + SystemUtils.LINE_SEPARATOR;
    }
}
