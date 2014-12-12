package com.github.nuclearg.nagisa.interceptor;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.CompilationUnit;

/**
 * 解释器
 * 
 * @author ng
 *
 */
public final class NagisaInterceptor {
    /**
     * 执行一段程序
     * 
     * @param cu
     *            待执行的程序
     * @param libs
     *            依赖的库
     */
    public static void eval(CompilationUnit cu, List<CompilationUnit> libs, List<CompilationUnit> rtlibs) {
        Context ctx = new Context();

        // 先加载运行库
        rtlibs.stream().forEach(lib -> new CompilationUnitInterceptor(lib, ctx));

        // 再加载用户库
        libs.stream().forEach(lib -> new CompilationUnitInterceptor(lib, ctx));

        // 再加载主程序
        CompilationUnitInterceptor main = new CompilationUnitInterceptor(cu, ctx);

        // 执行主程序
        main.eval(ctx);
    }
}
