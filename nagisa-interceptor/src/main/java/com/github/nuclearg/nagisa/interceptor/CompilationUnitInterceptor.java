package com.github.nuclearg.nagisa.interceptor;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.CompilationUnit;

/**
 * 整篇程序的解释器
 * 
 * @author ng
 *
 */
final class CompilationUnitInterceptor {
    private final List<StmtInterceptor> bodyStmts;

    CompilationUnitInterceptor(CompilationUnit cu, Context ctx) {
        // 主程序
        this.bodyStmts = StmtInterceptor.buildInterceptors(cu.getBodyStmts());

        // 函数列表
        cu.getFunctionStmts().forEach(stmt -> ctx.registerFunction(new FunctionInterceptor(stmt)));
    }

    /**
     * 执行整篇程序
     * 
     * @param ctx
     *            上下文
     */
    void eval(Context ctx) {
        StmtInterceptor.eval(this.bodyStmts, ctx);
    }

}
