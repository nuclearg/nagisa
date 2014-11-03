package com.github.nuclearg.nagisa.lang.ast.expr;

import com.github.nuclearg.nagisa.lang.ast.AstNode;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 表达式
 * 
 * @author ng
 *
 */
public abstract class Expr extends AstNode {

    public static Expr resolveExpr(SyntaxTreeNode syntaxTreeNode) {
        System.out.println(syntaxTreeNode.tokens);
        // 重整优先级
        return null;
    }

}
