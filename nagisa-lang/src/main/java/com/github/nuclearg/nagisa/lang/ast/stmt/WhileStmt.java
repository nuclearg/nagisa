package com.github.nuclearg.nagisa.lang.ast.stmt;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.ast.expr.Expr;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

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

    WhileStmt(SyntaxTreeNode node) {
        this.condition = Expr.resolveExpr(node.getChildren().get(1));
        this.stmts = Stmt.resolveStmts(node.getChildren().get(3).getChildren());
    }

    /** 循环判断条件 */
    public Expr getCondition() {
        return this.condition;
    }

    /** 循环体 */
    public List<Stmt> getStmts() {
        return this.stmts;
    }

    @Override
    public String toString(String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append("WHILE ").append(this.condition).append(SystemUtils.LINE_SEPARATOR);
        for (Stmt stmt : this.stmts)
            builder.append(stmt.toString(prefix + "    "));
        builder.append(prefix).append("WEND").append(SystemUtils.LINE_SEPARATOR);
        return builder.toString();
    }
}
