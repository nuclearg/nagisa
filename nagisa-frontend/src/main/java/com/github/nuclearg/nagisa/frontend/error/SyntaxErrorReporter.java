package com.github.nuclearg.nagisa.frontend.error;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;
import com.github.nuclearg.nagisa.frontend.util.Position;

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
        report(Position.EMPTY, errorType, args);
    }

    /**
     * 报告一个错误
     * 
     * @param range
     *            发生错误的位置
     * @param errorType
     *            要报告的错误类型
     * @param args
     *            参数列表
     */
    public void report(Position position, SyntaxErrorType errorType, Object... args) {
        SyntaxErrorLogItem error = new SyntaxErrorLogItem(errorType, args, position);
        this.errors.add(error);
        System.err.println(error);

        // 判断是否是致命错误
        if (errorType.level() == SyntaxErrorLevel.Fatal)
            throw new NagisaFrontEndFatalErrorException();

        // 判断是否超过上限
        if (this.errors.size() > ERROR_MAX_COUNT)
            report(position, Fatals.F1000);
    }

    /**
     * 报告一个错误
     * 
     * @param node
     *            发生错误的语法树节点
     * @param errorType
     *            要报告的错误类型
     * @param args
     *            参数列表
     */
    public void report(SyntaxTreeNode node, SyntaxErrorType errorType, Object... args) {
        report(node.getRange().getStartPosition(), errorType, args);
    }

    /** 错误列表 */
    public Iterable<SyntaxErrorLogItem> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

    /** 是否有错误 */
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    @Override
    public String toString() {
        return StringUtils.join(this.errors, LN);
    }
}
