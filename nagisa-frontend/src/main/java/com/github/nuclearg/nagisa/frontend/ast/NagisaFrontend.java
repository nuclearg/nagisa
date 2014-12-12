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
     * 将源代码解析为
     * 
     * @param text
     * @return
     */
    public static LoadResult loadCompilationUnit(String code) {
        SyntaxErrorReporter errorReporter = new SyntaxErrorReporter();
        IdentifierRegistry registry = new IdentifierRegistry(errorReporter);
        Context ctx = new Context(registry, errorReporter);

        SyntaxTreeNode node = NagisaSyntaxDefinition.parse(code, errorReporter);
        if (node == null)
            return new LoadResult(code, null, errorReporter.getErrors());

        CompilationUnit cu = new CompilationUnit(node, ctx);

        return new LoadResult(code, cu, errorReporter.getErrors());
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

        LoadResult(String code, CompilationUnit cu, List<SyntaxErrorLogItem> errors) {
            this.code = code;
            this.cu = cu;
            this.errors = Collections.unmodifiableList(errors);
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

    }
}
