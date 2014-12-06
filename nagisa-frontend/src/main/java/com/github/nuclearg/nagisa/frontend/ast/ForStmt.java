package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.identifier.IdentifierType;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * for循环语句
 * 
 * @author ng
 *
 */
public final class ForStmt extends Stmt implements StmtBlockSupported {
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
    private final StmtBlock stmts;

    ForStmt(SyntaxTreeNode node, Context ctx) {
        this.symbol = node.getChildren().get(1).getToken().getText();
        ctx.registry.registerVariableInfo(this.symbol, IdentifierType.INTEGER, node.getChildren().get(1));

        this.initValue = Expr.resolveExpr(node.getChildren().get(3), ctx);
        this.targetValue = Expr.resolveExpr(node.getChildren().get(5), ctx);
        this.stmts = new StmtBlock(node.getChildren().get(7).getChildren(), ctx);
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
    public void initStmtBlock() {
        this.stmts.init();
    }

    @Override
    public String toString(String prefix) {
        return prefix + "FOR " + this.symbol + " = " + this.initValue + " TO " + this.targetValue + SystemUtils.LINE_SEPARATOR
                + this.stmts.toString(prefix + "    ")
                + prefix + "NEXT" + SystemUtils.LINE_SEPARATOR;
    }

}
