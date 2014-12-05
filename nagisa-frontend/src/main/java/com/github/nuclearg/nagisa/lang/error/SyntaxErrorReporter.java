package com.github.nuclearg.nagisa.lang.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.nuclearg.nagisa.lang.lexer.Position;

/**
 * 语法树构建过程中的错误报告器
 * 
 * @author ng
 *
 */
public final class SyntaxErrorReporter {
    /**
     * 当发生的错误数高于此值时抛出一个致命错误，中断解析过程
     */
    private static final int ERROR_MAX_COUNT = 20;

    /**
     * 错误列表
     */
    private final List<SyntaxErrorLogItem> errors = new ArrayList<>();

    /**
     * 报告一个错误
     * 
     * @param errorType
     *            要报告的错误类型
     * @param args
     *            参数列表
     */
    public void report(SyntaxErrorType errorType, Object... args) {
        report(errorType, Position.EMPTY, args);
    }

    /**
     * 报告一个错误
     * 
     * @param errorType
     *            要报告的错误类型
     * @param position
     *            发生错误的位置
     * @param args
     *            参数列表
     */
    public void report(SyntaxErrorType errorType, Position position, Object... args) {
        SyntaxErrorLogItem error = new SyntaxErrorLogItem(errorType, args, position);
        this.errors.add(error);
        System.out.println(error);

        // 判断是否是致命错误
        if (errorType.level() == SyntaxErrorLevel.Fatal)
            throw new NagisaFrontEndFatalErrorException();

        // 判断是否超过上限
        if (this.errors.size() > ERROR_MAX_COUNT)
            report(Fatals.F1000, position);
    }

    /** 错误列表 */
    public List<SyntaxErrorLogItem> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

}
