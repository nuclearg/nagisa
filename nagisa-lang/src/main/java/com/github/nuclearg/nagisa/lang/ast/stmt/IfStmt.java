package com.github.nuclearg.nagisa.lang.ast.stmt;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.ast.expr.Expr;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 判断语句
 * 
 * @author ng
 *
 */
public class IfStmt extends Stmt {
    /**
     * 条件表达式
     */
    public final Expr condition;
    /**
     * 判断成功的操作
     */
    public final List<Stmt> thenStmts;
    /**
     * 判断失败的操作
     */
    public final List<Stmt> elseStmts;

    IfStmt(SyntaxTreeNode node) {
        this.condition = Expr.resolveExpr(node.children.get(1));

        this.thenStmts = Stmt.resolveStmts(node.children.get(4).children);
        this.elseStmts = null;
    }

    @Override
    public String toString(String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append("IF ").append(this.condition).append(" THEN");

        if (this.thenStmts.size() == 1)
            builder.append(" ").append(this.thenStmts.get(0));
        else {
            builder.append(SystemUtils.LINE_SEPARATOR);
            for (Stmt stmt : this.thenStmts)
                builder.append(stmt.toString(prefix + "    "));
            builder.append(prefix).append("ENDIF").append(SystemUtils.LINE_SEPARATOR);
        }

        return builder.toString();
    }
}
