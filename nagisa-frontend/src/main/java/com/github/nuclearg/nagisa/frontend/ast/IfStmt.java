package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 判断语句
 * 
 * @author ng
 *
 */
public final class IfStmt extends Stmt implements StmtBlockSupported {
    /**
     * 条件表达式
     */
    private final Expr condition;
    /**
     * 判断成功的操作
     */
    private final StmtBlock thenStmts;
    /**
     * 判断失败的操作
     */
    private final StmtBlock elseStmts;

    IfStmt(SyntaxTreeNode node, Context ctx) {
        this.condition = Expr.resolveExpr(node.getChildren().get(1), ctx);

        this.thenStmts = new StmtBlock(node.getChildren().get(4).getChildren(), ctx);

        if (!node.getChildren().get(7).getChildren().isEmpty())
            this.elseStmts = new StmtBlock(node.getChildren().get(7).getChildren(), ctx);
        else
            this.elseStmts = new StmtBlock(Collections.emptyList(), ctx);
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
    public void initStmtBlock() {
        this.thenStmts.init();
        this.elseStmts.init();
    }

    @Override
    public String toString(String prefix) {
        return prefix + "IF " + this.condition + " THEN" + SystemUtils.LINE_SEPARATOR
                + this.thenStmts.toString(prefix + "    ")
                + prefix + "ELSE" + SystemUtils.LINE_SEPARATOR
                + this.elseStmts.toString(prefix + "    ")
                + prefix + "END IF" + SystemUtils.LINE_SEPARATOR;
    }
}
