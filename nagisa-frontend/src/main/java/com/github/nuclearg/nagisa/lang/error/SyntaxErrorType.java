package com.github.nuclearg.nagisa.lang.error;

/**
 * 语法错误类型
 * 
 * @author ng
 *
 */
public interface SyntaxErrorType {
    /**
     * 错误级别
     */
    public SyntaxErrorLevel level();

    /**
     * 错误编号
     */
    public String name();

    /**
     * 错误内容
     */
    public String message();
}
