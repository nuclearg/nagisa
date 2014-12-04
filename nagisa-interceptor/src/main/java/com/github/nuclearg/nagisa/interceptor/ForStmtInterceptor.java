package com.github.nuclearg.nagisa.interceptor;

import java.util.List;

import com.github.nuclearg.nagisa.lang.ast.ForStmt;

/**
 * for循环语句的解释器
 * 
 * @author ng
 *
 */
class ForStmtInterceptor extends StmtInterceptor {
    /**
     * 循环变量名称
     */
    private final String iteratorVariableName;
    /**
     * 初始值
     */
    private final ExprInterceptor initValue;
    /**
     * 目标值
     */
    private final ExprInterceptor targetValue;
    /**
     * 循环体
     */
    private final List<StmtInterceptor> stmts;

    ForStmtInterceptor(ForStmt stmt) {
        this.iteratorVariableName = stmt.getIteratorVariableName();
        this.initValue = ExprInterceptor.buildInterceptor(stmt.getInitValue());
        this.targetValue = ExprInterceptor.buildInterceptor(stmt.getTargetValue());
        this.stmts = StmtInterceptor.buildInterceptors(stmt.getStmts());
    }

    @Override
    public void eval(Context ctx) {
        long value = this.initValue.eval(ctx).getIntegerValue();
        ctx.setVariableValue(this.iteratorVariableName, new Value(value));

        while (ctx.getIntegerVariableValue(this.iteratorVariableName) < this.targetValue.eval(ctx).getIntegerValue())
            StmtInterceptor.eval(this.stmts, ctx);
    }

}
