package com.github.nuclearg.nagisa.lang.ast;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 简单循环语句
 * 
 * @author ng
 *
 */
public final class ForStmt extends Stmt {
    /**
     * 循环变量名
     */
    private final String symbol;
    /**
     * 初始值
     */
    private final Expr initValue;
    /**
     * 目标值
     */
    private final Expr targetValue;
    /**
     * 循环体
     */
    private final List<Stmt> stmts;

    ForStmt(SyntaxTreeNode node) {
        this.symbol = node.getChildren().get(1).getToken().getText();
        this.initValue = Expr.resolveExpr(node.getChildren().get(3));
        this.targetValue = Expr.resolveExpr(node.getChildren().get(5));
        this.stmts = Stmt.resolveStmts(node.getChildren().get(7).getChildren());
    }

    /** 循环变量名 */
    public String getSymbol() {
        return this.symbol;
    }

    /** 初始值 */
    public Expr getInitValue() {
        return this.initValue;
    }

    /** 目标值 */
    public Expr getTargetValue() {
        return this.targetValue;
    }

    /** 循环体 */
    public List<Stmt> getStmts() {
        return this.stmts;
    }

    @Override
    public String toString(String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append("FOR ").append(this.symbol).append(" = ").append(this.initValue).append(" TO ").append(this.targetValue).append(SystemUtils.LINE_SEPARATOR);
        for (Stmt stmt : this.stmts)
            builder.append(stmt.toString(prefix + "    "));
        builder.append(prefix).append("NEXT").append(SystemUtils.LINE_SEPARATOR);
        return builder.toString();
    }
}
