package com.github.nuclearg.nagisa.interceptor;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.WhileStmt;

/**
 * while循环语句的解释器
 * 
 * @author ng
 *
 */
class WhileStmtInterceptor extends StmtInterceptor {
    /**
     * 条件表达式
     */
    private final ExprInterceptor condition;
    /**
     * 循环体
     */
    private final List<StmtInterceptor> stmts;

    WhileStmtInterceptor(WhileStmt stmt) {
        this.condition = ExprInterceptor.buildInterceptor(stmt.getCondition());
        this.stmts = StmtInterceptor.buildInterceptors(stmt.getStmts());
    }

    @Override
    public void eval(Context ctx) {
        while (this.condition.eval(ctx).getBooleanValue())
            StmtInterceptor.eval(this.stmts, ctx);
    }

}
