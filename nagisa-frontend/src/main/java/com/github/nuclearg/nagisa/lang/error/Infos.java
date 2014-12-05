package com.github.nuclearg.nagisa.lang.error;

/**
 * 普通输出内容定义
 * 
 * @author ng
 *
 */
public enum Infos implements SyntaxErrorType {
    I0001(""),

    ;
    private final String message;

    private Infos(String message) {
        this.message = message;
    }

    @Override
    public SyntaxErrorLevel level() {
        return SyntaxErrorLevel.Info;
    }

    @Override
    public String message() {
        return this.message;
    }
}
