package com.github.nuclearg.nagisa.interceptor;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.ForStmt;
import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;
import com.github.nuclearg.nagisa.interceptor.ex.NagisaInterceptorInternalException;

/**
 * for循环语句的解释器
 * 
 * @author ng
 *
 */
final class ForStmtInterceptor extends StmtInterceptor {
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
        if (stmt.getInitValue().getType() != TypeIdentifierInfo.INTEGER)
            throw new NagisaInterceptorInternalException("if语句的表达式不是integer类型. expr: " + stmt.getInitValue());
        if (stmt.getTargetValue().getType() != TypeIdentifierInfo.INTEGER)
            throw new NagisaInterceptorInternalException("if语句的表达式不是integer类型. expr: " + stmt.getTargetValue());

        this.iteratorVariableName = stmt.getIteratorVariableName();
        this.initValue = ExprInterceptor.buildInterceptor(stmt.getInitValue());
        this.targetValue = ExprInterceptor.buildInterceptor(stmt.getTargetValue());
        this.stmts = StmtInterceptor.buildInterceptors(stmt.getStmts());
    }

    @Override
    public void eval(Context ctx) {
        long value = (long) this.initValue.eval(ctx).getValue();
        ctx.setVariableValue(this.iteratorVariableName, new Value(value, TypeIdentifierInfo.INTEGER));

        while (true) {
            long indexValue = ctx.getIntegerVariableValue(this.iteratorVariableName);
            Value targetValue = this.targetValue.eval(ctx);
            if (targetValue.getType() != TypeIdentifierInfo.INTEGER)
                throw new NagisaInterceptorInternalException("for语句的target表达式计算结果不是integer类型. expr: " + this.targetValue + ", value: " + targetValue);

            if (indexValue >= (Long) targetValue.getValue())
                return;

            StmtInterceptor.eval(this.stmts, ctx);

            ctx.setVariableValue(this.iteratorVariableName, new Value(indexValue + 1, TypeIdentifierInfo.INTEGER));
        }
    }

}
