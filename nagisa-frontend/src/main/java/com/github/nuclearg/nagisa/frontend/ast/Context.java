package com.github.nuclearg.nagisa.frontend.ast;

import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.identifier.IdentifierRegistry;

/**
 * 构建语法树时使用的上下文
 * 
 * @author ng
 *
 */
final class Context {
    /**
     * 标识符注册表
     */
    final IdentifierRegistry registry;
    /**
     * 错误报告器
     */
    final SyntaxErrorReporter errorReporter;

    Context(IdentifierRegistry registry, SyntaxErrorReporter errorReporter) {
        this.registry = registry;
        this.errorReporter = errorReporter;
    }

}
