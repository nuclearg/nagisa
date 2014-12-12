package com.github.nuclearg.nagisa.interceptor;

import com.github.nuclearg.nagisa.frontend.ast.ReturnStmt;
import com.github.nuclearg.nagisa.interceptor.ex.FunctionReturnException;

/**
 * 返回语句的解释器
 * 
 * @author ng
 *
 */
final class ReturnStmtInterceptor extends StmtInterceptor {
    private final ExprInterceptor expr;

    ReturnStmtInterceptor(ReturnStmt stmt) {
        if (stmt.getExpr() != null)
            this.expr = ExprInterceptor.buildInterceptor(stmt.getExpr());
        else
            this.expr = null;
    }

    @Override
    protected void eval(Context ctx) {
        if (this.expr != null)
            ctx.setVariableValue("$RET_VALUE", this.expr.eval(ctx));

        throw new FunctionReturnException();
    }

}
