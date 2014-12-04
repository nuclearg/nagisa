package com.github.nuclearg.nagisa.lang.error;

/**
 * 错误级别
 * 
 * @author ng
 *
 */
public enum SyntaxErrorLevel {
    /**
     * 致命错误，这将直接中断
     */
    Fatal,
    /**
     * 编译错误
     */
    Error,
    /**
     * 编译警告
     */
    Warning,
    /**
     * 一些普通的输出信息
     */
    Info,
}
