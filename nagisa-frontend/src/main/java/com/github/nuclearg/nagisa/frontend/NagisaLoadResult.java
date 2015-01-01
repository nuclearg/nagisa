package com.github.nuclearg.nagisa.frontend;

import com.github.nuclearg.nagisa.frontend.ast.Program;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorLogItem;

/**
 * 程序加载的结果
 * 
 * @author ng
 *
 */
public final class NagisaLoadResult {
    /**
     * 解析是否成功
     */
    private final boolean success;

    /**
     * 加载好的程序，如果报了语法错则此值为null
     */
    private final Program program;
    /**
     * 解析过程中报的错误
     */
    private final Iterable<SyntaxErrorLogItem> errors;

    NagisaLoadResult(Program program) {
        this.success = true;
        this.program = program;
        this.errors = null;
    }

    NagisaLoadResult(Iterable<SyntaxErrorLogItem> errors) {
        this.success = false;
        this.program = null;
        this.errors = errors;
    }

    /** 解析是否成功 */
    public boolean isSuccess() {
        return this.success;
    }

    /** 加载好的程序，如果报了语法错则此值为null */
    public Program getProgram() {
        return this.program;
    }

    /** 解析过程中报的错误 */
    public Iterable<SyntaxErrorLogItem> getErrors() {
        return this.errors;
    }

}
