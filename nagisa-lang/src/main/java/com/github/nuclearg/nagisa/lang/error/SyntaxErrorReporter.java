package com.github.nuclearg.nagisa.lang.error;

import java.util.Locale;

import com.github.nuclearg.nagisa.lang.lexer.Position;
import com.github.nuclearg.nagisa.lang.parser.SyntaxParserFatalErrorException;

/**
 * 语法树构建过程中的错误报告器
 * 
 * @author ng
 *
 */
public final class SyntaxErrorReporter {
    private int errorCount;

    /**
     * 报告一个错误
     * 
     * @param item
     *            要报告的错误
     */
    public void report(SyntaxErrorReportItem item, Object... args) {
        report(item, Position.EMPTY, args);
    }

    /**
     * 报告一个错误
     * 
     * @param item
     *            要报告的错误
     * @param position
     *            发生错误的位置
     */
    public void report(SyntaxErrorReportItem item, Position position, Object... args) {
        String message = position + " " + item.name() + ": " + String.format(Locale.getDefault(), item.message(), args);

        switch (item.level()) {
            case Fatal:
                System.err.println("FATAL " + message);
                throw new SyntaxParserFatalErrorException(message, position);
            case Error:
                System.err.println("ERROR " + message);

                if (++errorCount >= 20)
                    report(Fatals.F1000, position);
                break;
            case Warning:
                System.err.println("WARN  " + message);
                break;
            case Info:
                System.err.println("INFO  " + message);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
