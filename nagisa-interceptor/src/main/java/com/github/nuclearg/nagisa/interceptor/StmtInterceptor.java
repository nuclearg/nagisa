package com.github.nuclearg.nagisa.interceptor;

import java.util.ArrayList;
import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.Stmt;

/**
 * 语句的解释器
 * 
 * @author ng
 *
 */
abstract class StmtInterceptor {
    /**
     * 执行语句
     * 
     * @param ctx
     *            上下文
     */
    protected abstract void eval(Context ctx);

    /**
     * 根据一系列语句列表创建对应的解释器
     * 
     * @param stmts
     *            语句列表
     * @return 解释器列表
     */
    public static List<StmtInterceptor> buildInterceptors(Iterable<Stmt> stmts) {
        List<StmtInterceptor> interceptors = new ArrayList<>();

        stmts.forEach(s -> interceptors.add(buildInterceptor(s)));

        return interceptors;
    }

    /**
     * 构造单条语句的解释器
     * 
     * @param stmt
     *            语句
     * @return 对应的解释器
     */
    private static StmtInterceptor buildInterceptor(Stmt stmt) {
        return new EmptyStmtInterceptor();
    }

    /**
     * 执行多个语句
     * 
     * @param stmts
     *            语句列表
     * @param ctx
     *            上下文
     */
    static void eval(Iterable<StmtInterceptor> stmts, Context ctx) {
        stmts.forEach(stmt -> stmt.eval(ctx));
    }
}
