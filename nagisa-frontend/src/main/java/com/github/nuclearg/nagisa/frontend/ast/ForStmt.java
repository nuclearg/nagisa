package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;
import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.TAB;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;
import com.github.nuclearg.nagisa.frontend.symbol.TypeSymbol;

/**
 * for循环语句
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

    ForStmt(SyntaxTreeNode node, Context ctx) {
        this.symbol = node.getChildren().get(1).getToken().getText();
        ctx.getRegistry().registerVariableInfo(this.symbol, TypeSymbol.INTEGER, node.getChildren().get(1));

        this.initValue = Expr.buildExpr(node.getChildren().get(3), ctx);
        this.targetValue = Expr.buildExpr(node.getChildren().get(5), ctx);
        this.stmts = Stmt.buildStmts(node.getChildren().get(7).getChildren(), ctx);
    }

    /** 循环变量名 */
    public String getIteratorVariableName() {
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
    public Iterable<Stmt> getStmts() {
        return this.stmts;
    }

    @Override
    public String toString(String prefix) {
        return prefix + "FOR " + this.symbol + " = " + this.initValue + " TO " + this.targetValue + LN
                + Stmt.toString(this.stmts, prefix + TAB)
                + prefix + "NEXT" + LN;
    }

}
