package com.github.nuclearg.nagisa.interceptor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.github.nuclearg.nagisa.frontend.ast.CallSubStmt;
import com.github.nuclearg.nagisa.frontend.ast.DefineVariableStmt;
import com.github.nuclearg.nagisa.frontend.ast.EmptyStmt;
import com.github.nuclearg.nagisa.frontend.ast.ForStmt;
import com.github.nuclearg.nagisa.frontend.ast.IfStmt;
import com.github.nuclearg.nagisa.frontend.ast.ReturnStmt;
import com.github.nuclearg.nagisa.frontend.ast.Stmt;
import com.github.nuclearg.nagisa.frontend.ast.VariableSetStmt;
import com.github.nuclearg.nagisa.frontend.ast.WhileStmt;
import com.github.nuclearg.nagisa.interceptor.ex.NagisaInterceptorInternalException;

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
        if (stmt instanceof CallSubStmt)
            return new CallSubStmtInterceptor((CallSubStmt) stmt);
        if (stmt instanceof DefineVariableStmt)
            return new DefineVariableStmtInterceptor((DefineVariableStmt) stmt);
        if (stmt instanceof EmptyStmt)
            return new EmptyStmtInterceptor();
        if (stmt instanceof ForStmt)
            return new ForStmtInterceptor((ForStmt) stmt);
        if (stmt instanceof IfStmt)
            return new IfStmtInterceptor((IfStmt) stmt);
        if (stmt instanceof ReturnStmt)
            return new ReturnStmtInterceptor((ReturnStmt) stmt);
        if (stmt instanceof VariableSetStmt)
            return new VariableSetStmtInterceptor((VariableSetStmt) stmt);
        if (stmt instanceof WhileStmt)
            return new WhileStmtInterceptor((WhileStmt) stmt);

        throw new NagisaInterceptorInternalException("unsupported stmt. stmt: " + stmt);
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
