package com.github.nuclearg.nagisa.interceptor;

import com.github.nuclearg.nagisa.frontend.ast.VariableSetStmt;

/**
 * 赋值循环语句的解释器
 * 
 * @author ng
 *
 */
class VariableSetStmtInterceptor extends StmtInterceptor {
    /**
     * 变量名称
     */
    private final String variableName;
    /**
     * 表达式
     */
    private final ExprInterceptor expr;

    VariableSetStmtInterceptor(VariableSetStmt stmt) {
        this.variableName = stmt.getVariableName();
        this.expr = ExprInterceptor.buildInterceptor(stmt.getExpr());
    }

    @Override
    public void eval(Context ctx) {
        Value value = expr.eval(ctx);
        ctx.setVariableValue(this.variableName, value);
    }

}
