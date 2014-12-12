package com.github.nuclearg.nagisa.interceptor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.github.nuclearg.nagisa.frontend.ast.Stmt;

/**
 * 语句的解释器
 * 
 * @author ng
 *
 */
abstract class StmtInterceptor {

    StmtInterceptor() {
        // 禁止这个包外面的类继承这个类
    }

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
        return StreamSupport.stream(stmts.spliterator(), false)
                .map(s -> buildInterceptor(s))
                .collect(Collectors.toList());
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
