package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;
import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.TAB;

import java.util.Collections;
import java.util.List;

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
        this.condition = Expr.buildExpr(node.getChildren().get(1), ctx);

        this.thenStmts = Stmt.buildStmts(node.getChildren().get(4).getChildren(), ctx);

        if (!node.getChildren().get(7).getChildren().isEmpty())
            this.elseStmts = Stmt.buildStmts(node.getChildren().get(7).getChildren(), ctx);
        else
            this.elseStmts = Stmt.buildStmts(Collections.emptyList(), ctx);
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
        return prefix + "IF " + this.condition + " THEN" + LN
                + Stmt.toString(this.thenStmts, prefix + TAB)
                + prefix + "ELSE" + LN
                + Stmt.toString(this.elseStmts, prefix + TAB)
                + prefix + "END IF" + LN;
    }
}
