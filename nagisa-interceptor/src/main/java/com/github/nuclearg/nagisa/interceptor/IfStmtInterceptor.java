package com.github.nuclearg.nagisa.interceptor;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.IfStmt;

/**
 * if循环语句的解释器
 * 
 * @author ng
 *
 */
class IfStmtInterceptor extends StmtInterceptor {
    /**
     * 条件表达式
     */
    private final ExprInterceptor condition;
    /**
     * 条件满足时的语句列表
     */
    private final List<StmtInterceptor> thenStmts;
    /**
     * 条件不满足时的语句列表
     */
    private final List<StmtInterceptor> elseStmts;

    IfStmtInterceptor(IfStmt stmt) {
        this.condition = ExprInterceptor.buildInterceptor(stmt.getCondition());
        this.thenStmts = StmtInterceptor.buildInterceptors(stmt.getThenStmts());
        this.elseStmts = StmtInterceptor.buildInterceptors(stmt.getElseStmts());
    }

    @Override
    public void eval(Context ctx) {
        Value value = this.condition.eval(ctx);

        if (value.getBooleanValue())
            StmtInterceptor.eval(this.thenStmts, ctx);
        else
            StmtInterceptor.eval(this.elseStmts, ctx);
    }
}
