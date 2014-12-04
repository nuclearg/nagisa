package com.github.nuclearg.nagisa.lang.error;

/**
 * 警告定义
 * 
 * @author ng
 *
 */
public enum Warns implements SyntaxErrorReportItem {
    W0001(""),

    ;
    private final String message;

    private Warns(String message) {
        this.message = message;
    }

    @Override
    public SyntaxErrorLevel level() {
        return SyntaxErrorLevel.Warning;
    }

    @Override
    public String message() {
        return this.message;
    }
}
