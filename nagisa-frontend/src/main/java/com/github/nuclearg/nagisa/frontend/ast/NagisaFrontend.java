package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;

import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorLogItem;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.identifier.IdentifierRegistry;
import com.github.nuclearg.nagisa.frontend.parser.NagisaSyntaxDefinition;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * nagisa前端功能暴露点
 * 
 * @author ng
 *
 */
public final class NagisaFrontend {
    /**
     * 解析源代码
     * 
     * @param code
     *            源代码
     * @return 解析结果
     */
    public static LoadResult loadCompilationUnit(String code) {
        SyntaxErrorReporter errorReporter = new SyntaxErrorReporter();
        IdentifierRegistry registry = new IdentifierRegistry(errorReporter);
        Context ctx = new Context(registry, errorReporter);

        SyntaxTreeNode node = NagisaSyntaxDefinition.parse(code, errorReporter);
        if (node == null)
            return new LoadResult(code, null, errorReporter.getErrors(), ctx);

        CompilationUnit cu = new CompilationUnit(node, ctx);

        return new LoadResult(code, cu, errorReporter.getErrors(), ctx);
    }

    /**
     * 在上一个文件的解析结果基础上解析源代码
     * 
     * @param code
     *            源代码
     * @param prevResult
     *            上一个文件的解析结果
     * @return 解析结果
     */
    public static LoadResult loadCompilationUnit(String code, LoadResult prevResult) {
        Context ctx = prevResult.getCtx().clone();

        SyntaxTreeNode node = NagisaSyntaxDefinition.parse(code, ctx.errorReporter);
        if (node == null)
            return new LoadResult(code, null, ctx.errorReporter.getErrors(), ctx);

        CompilationUnit cu = new CompilationUnit(node, ctx);

        return new LoadResult(code, cu, ctx.errorReporter.getErrors(), ctx);
    }

    /**
     * 源代码解析结果
     * 
     * @author ng
     *
     */
    public static final class LoadResult {
        /**
         * 被解析的源代码
         */
        private final String code;
        /**
         * 解析好的源文件，如果报了语法错则此值为null
         */
        private final CompilationUnit cu;
        /**
         * 解析过程中报的错误列表
         */
        private final List<SyntaxErrorLogItem> errors;

        /**
         * 上下文
         */
        private final Context ctx;

        LoadResult(String code, CompilationUnit cu, List<SyntaxErrorLogItem> errors, Context ctx) {
            this.code = code;
            this.cu = cu;
            this.errors = Collections.unmodifiableList(errors);
            this.ctx = ctx;
        }

        /** 被解析的源代码 */
        public String getCode() {
            return this.code;
        }

        /** 解析好的源文件，如果报了语法错则此值为null */
        public CompilationUnit getCu() {
            return this.cu;
        }

        /** 解析过程中报的错误列表 */
        public List<SyntaxErrorLogItem> getErrors() {
            return this.errors;
        }

        /** 上下文 */

        Context getCtx() {
            return this.ctx;
        }
    }
}
