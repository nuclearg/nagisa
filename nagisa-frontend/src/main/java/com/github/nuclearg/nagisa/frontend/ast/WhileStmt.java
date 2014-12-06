package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * while循环语句
 * 
 * @author ng
 *
 */
public final class WhileStmt extends Stmt implements StmtBlockSupported {
    /**
     * 循环判断条件
     */
    private final Expr condition;
    /**
     * 循环体
     */
    private final StmtBlock stmts;

    WhileStmt(SyntaxTreeNode node, Context ctx) {
        this.condition = Expr.resolveExpr(node.getChildren().get(1), ctx);
        this.stmts = new StmtBlock(node.getChildren().get(3).getChildren(), ctx);
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
    public void initStmtBlock() {
        this.stmts.init();
    }

    @Override
    public String toString(String prefix) {
        return prefix + "WHILE " + this.condition + SystemUtils.LINE_SEPARATOR
                + this.stmts.toString(prefix + "    ")
                + prefix + "END WHILE" + SystemUtils.LINE_SEPARATOR;
    }
}
