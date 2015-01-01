package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Stack;

import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.symbol.SymbolRegistry;

/**
 * 构建语法树时使用的上下文
 * 
 * @author ng
 *
 */
final class Context {
    /**
     * 符号注册表
     */
    private final Stack<SymbolRegistry> registryStack;
    /**
     * 错误报告器
     */
    private final SyntaxErrorReporter errorReporter;

    /**
     * 当前的遍数
     */
    private final ScanPhase phase;

    Context(SyntaxErrorReporter errorReporter, ScanPhase phase) {
        this.registryStack = new Stack<>();
        this.registryStack.push(new SymbolRegistry(errorReporter));
        this.errorReporter = errorReporter;
        this.phase = phase;
    }

    Context(Context ctx, ScanPhase phase) {
        this.registryStack = ctx.registryStack;
        this.errorReporter = ctx.errorReporter;
        this.phase = phase;
    }

    /** 符号注册表 */
    SymbolRegistry getRegistry() {
        return this.registryStack.peek();
    }

    /** 错误报告器 */
    SyntaxErrorReporter getErrorReporter() {
        return this.errorReporter;
    }

    /** 当前的遍数 */
    ScanPhase getPhase() {
        return this.phase;
    }

    /**
     * 增加一个层次
     */
    void pushLevel(boolean copyParentVariables) {
        SymbolRegistry registry = new SymbolRegistry(this.registryStack.peek(), copyParentVariables);
        this.registryStack.push(registry);
    }

    /**
     * 减少一个层次
     */
    void popLevel() {
        this.registryStack.pop();
    }
}
