package com.github.nuclearg.nagisa.interceptor;

import java.util.List;
import java.util.stream.Collectors;

import com.github.nuclearg.nagisa.frontend.ast.CallSubStmt;

/**
 * 调用方法语句的解释器
 * 
 * @author ng
 *
 */
final class CallSubStmtInterceptor extends StmtInterceptor {
    /**
     * 方法名
     */
    private final String name;
    private final List<ExprInterceptor> arguments;

    CallSubStmtInterceptor(CallSubStmt stmt) {
        this.name = stmt.getName();
        this.arguments = ExprInterceptor.buildInterceptors(stmt.getArguments());
    }

    @Override
    public void eval(Context ctx) {
        List<Value> values = this.arguments.stream()
                .map(e -> e.eval(ctx))
                .collect(Collectors.toList());

        ctx.invokeFunction(this.name, values);
    }
}
