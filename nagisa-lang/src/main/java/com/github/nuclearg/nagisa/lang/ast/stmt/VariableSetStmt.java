package com.github.nuclearg.nagisa.lang.ast.stmt;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.ast.expr.Expr;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 赋值语句
 * 
 * @author ng
 *
 */
public class VariableSetStmt extends Stmt {
    /**
     * 变量名
     */
    public final String symbol;
    /**
     * 表达式
     */
    public final Expr expr;

    VariableSetStmt(SyntaxTreeNode node) {
        this.symbol = node.tokens.get(1).text;
        this.expr = Expr.resolveExpr(node.children.get(3));
    }

    @Override
    public String toString(String prefix) {
        return prefix + "LET " + this.symbol + " = " + this.expr + SystemUtils.LINE_SEPARATOR;
    }
}
