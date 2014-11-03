package com.github.nuclearg.nagisa.lang.ast.stmt;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.ast.expr.Expr;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

public class WhileStmt extends Stmt {
    public final Expr condition;
    public final List<Stmt> stmts;

    WhileStmt(SyntaxTreeNode node) {
        this.condition = Expr.resolveExpr(node.children.get(1));
        this.stmts = Stmt.resolveStmts(node.children.get(3).children);
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
