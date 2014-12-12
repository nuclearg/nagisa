package com.github.nuclearg.nagisa.interceptor;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.WhileStmt;
import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;
import com.github.nuclearg.nagisa.interceptor.ex.NagisaInterceptorInternalException;

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
        if (stmt.getCondition().getType() != TypeIdentifierInfo.BOOLEAN)
            throw new NagisaInterceptorInternalException("while语句的表达式不是boolean类型. expr: " + stmt.getCondition());

        this.condition = ExprInterceptor.buildInterceptor(stmt.getCondition());
        this.stmts = StmtInterceptor.buildInterceptors(stmt.getStmts());
    }

    @Override
    public void eval(Context ctx) {
        while (true) {
            Value value = this.condition.eval(ctx);
            if (value.getType() != TypeIdentifierInfo.BOOLEAN)
                throw new NagisaInterceptorInternalException("while语句的表达式计算结果不是boolean类型. expr: " + this.condition + ", value: " + value);

            if ((Boolean) value.getValue())
                StmtInterceptor.eval(this.stmts, ctx);
            else
                return;
        }
    }

}
