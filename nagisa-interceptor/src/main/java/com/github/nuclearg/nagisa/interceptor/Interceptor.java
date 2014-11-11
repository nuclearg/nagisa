package com.github.nuclearg.nagisa.interceptor;

import com.github.nuclearg.nagisa.lang.ast.CompilationUnit;

/**
 * 解释器
 * 
 * @author ng
 *
 */
public final class Interceptor {
    /**
     * 执行一段程序
     * 
     * @param cu
     *            待执行的程序
     */
    public void eval(CompilationUnit cu) {
        new CompilationUnitInterceptor(cu).eval(new Context());
    }
}
