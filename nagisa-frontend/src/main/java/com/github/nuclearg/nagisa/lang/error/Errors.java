package com.github.nuclearg.nagisa.lang.error;

/**
 * 错误定义
 * 
 * @author ng
 *
 */
public enum Errors implements SyntaxErrorType {

    E0001("遇到意外的符号 %s，期望 %s"),
    E0002("遇到意外的符号 %s，无法推断语法"),

    E1001("变量 %s 重复定义，且类型不一致。原始类型为 %s，试图重定义的类型为 %s"),
    E1002("变量 %s 未定义"),
    E1003("变量 %s 类型不匹配。期望类型为 %s 但实际类型为 %s"),
    E1004("类型不匹配。期望类型为 %s 但实际类型为 %s"),
    E1005("类型不匹配。尝试对 %s 和 %s 两种类型的表达式进行运算"),
    E1006("无法对 %s 类型的表达式进行 %s 运算"),
    E1007("不能直接使用 VOID 类型"),

    E2001("函数或方法 %s 重复定义"),
    E2002("函数或方法 %s 未定义"),
    E2003("不能在表达式中调用方法 %s。对方法的调用必须是一条独立的语句"),
    E2004("调用函数或方法 %s 时，提供的参数数量与期望的参数数量不匹配"),
    E2005("调用函数或方法 %s 时，第 %s 个参数的类型不匹配。期望类型为 %s 但实际类型为 %s"),

    E3001("类型 %s 未定义"),
    E3002("类型 %s 重复定义"),

    ;
    private final String message;

    private Errors(String message) {
        this.message = message;
    }

    @Override
    public SyntaxErrorLevel level() {
        return SyntaxErrorLevel.Error;
    }

    @Override
    public String message() {
        return this.message;
    }
}
