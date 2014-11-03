package com.github.nuclearg.nagisa.lang.ast.expr;

import com.github.nuclearg.nagisa.lang.ast.AstNode;
import com.github.nuclearg.nagisa.lang.ast.expr.bool.BooleanExpr;
import com.github.nuclearg.nagisa.lang.ast.expr.number.NumberExpr;
import com.github.nuclearg.nagisa.lang.ast.expr.string.StringExpr;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 表达式
 * 
 * @author ng
 *
 */
public abstract class Expr extends AstNode {

    public static Expr resolveExpr(SyntaxTreeNode node) {
        switch (node.ruleName) {
            case "NumberExpr":
                return NumberExpr.resolveExpr(node);
            case "StringExpr":
                return StringExpr.resolveExpr(node);
            case "BooleanExpr":
                return BooleanExpr.resolveExpr(node);
            default:
                throw new UnsupportedOperationException(node.ruleName);
        }
    }

}
