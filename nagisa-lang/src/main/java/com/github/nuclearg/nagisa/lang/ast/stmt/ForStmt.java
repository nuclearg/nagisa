package com.github.nuclearg.nagisa.lang.ast.stmt;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.ast.expr.Expr;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

public class ForStmt extends Stmt {
    public final String symbol;
    public final Expr initValue;
    public final Expr targetValue;
    public final List<Stmt> stmts;

    ForStmt(SyntaxTreeNode node) {
        this.symbol = node.tokens.get(1).text;
        this.initValue = Expr.resolveExpr(node.children.get(3));
        this.targetValue = Expr.resolveExpr(node.children.get(5));
        this.stmts = Stmt.resolveStmts(node.children.get(7).children);
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
