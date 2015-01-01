package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 赋值语句
 * 
 * @author ng
 *
 */
public final class VariableSetStmt extends Stmt {
    /**
     * 变量名
     */
    private final String symbol;
    /**
     * 表达式
     */
    private final Expr expr;

    VariableSetStmt(SyntaxTreeNode node, Context ctx) {
        this.symbol = node.getChildren().get(1).getToken().getText();
        this.expr = Expr.buildExpr(node.getChildren().get(3), ctx);

        if (this.expr == null)
            return;

        // 注册变量名
        ctx.getRegistry().registerVariableInfo(this.symbol, this.expr.getType(), node);
    }

    /** 变量名 */
    public String getVariableName() {
        return this.symbol;
    }

    /** 表达式 */
    public Expr getExpr() {
        return this.expr;
    }

    @Override
    public String toString(String prefix) {
        return prefix + "LET " + this.symbol + " = " + this.expr + LN;
    }
}
