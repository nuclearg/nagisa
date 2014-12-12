package com.github.nuclearg.nagisa.interceptor;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.IfStmt;
import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;
import com.github.nuclearg.nagisa.interceptor.ex.NagisaInterceptorInternalException;

/**
 * if循环语句的解释器
 * 
 * @author ng
 *
 */
final class IfStmtInterceptor extends StmtInterceptor {
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
        if (stmt.getCondition().getType() != TypeIdentifierInfo.BOOLEAN)
            throw new NagisaInterceptorInternalException("if语句的表达式不是boolean类型. expr: " + stmt.getCondition());

        this.condition = ExprInterceptor.buildInterceptor(stmt.getCondition());
        this.thenStmts = StmtInterceptor.buildInterceptors(stmt.getThenStmts());
        this.elseStmts = StmtInterceptor.buildInterceptors(stmt.getElseStmts());

    }

    @Override
    public void eval(Context ctx) {
        Value value = this.condition.eval(ctx);
        if (value.getType() != TypeIdentifierInfo.BOOLEAN)
            throw new NagisaInterceptorInternalException("if语句的表达式计算结果不是boolean类型. expr: " + this.condition + ", value: " + value);

        if ((Boolean) value.getValue())
            StmtInterceptor.eval(this.thenStmts, ctx);
        else
            StmtInterceptor.eval(this.elseStmts, ctx);
    }
}
