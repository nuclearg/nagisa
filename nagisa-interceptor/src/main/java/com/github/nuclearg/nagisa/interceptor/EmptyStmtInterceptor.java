package com.github.nuclearg.nagisa.interceptor;

/**
 * 空循环语句的解释器
 * 
 * @author ng
 *
 */
final class EmptyStmtInterceptor extends StmtInterceptor {
    @Override
    public void eval(Context ctx) {
        // do nothing
    }

}
