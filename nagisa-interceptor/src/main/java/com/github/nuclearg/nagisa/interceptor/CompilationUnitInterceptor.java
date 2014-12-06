package com.github.nuclearg.nagisa.interceptor;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.CompilationUnit;

/**
 * 整篇程序的解释器
 * 
 * @author ng
 *
 */
class CompilationUnitInterceptor {
    private final List<StmtInterceptor> bodyStmts;
    private final List<StmtInterceptor> functionStmts;

    CompilationUnitInterceptor(CompilationUnit cu) {
        this.bodyStmts = StmtInterceptor.buildInterceptors(cu.getBodyStmts());
        this.functionStmts = StmtInterceptor.buildInterceptors(cu.getBodyStmts());
    }

    /**
     * 执行整篇程序
     * 
     * @param ctx
     *            上下文
     */
    void eval(Context ctx) {
        StmtInterceptor.eval(this.functionStmts, ctx);
        StmtInterceptor.eval(this.bodyStmts, ctx);
    }

}
