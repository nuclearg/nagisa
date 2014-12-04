package com.github.nuclearg.nagisa.lang.error;

/**
 * 致命错误定义
 * <p>
 * <li>F0000~F0999 IO相关的错误</li>
 * <li>F1000~F1999 编译相关的错误</li>
 * </p>
 * 
 * @author ng
 *
 */
public enum Fatals implements SyntaxErrorReportItem {
    F0001("源文件读取失败"),

    F1000("语法错误过多，编译过程中断"),
    F1001("有剩余的字符未被解析"),

    ;
    private final String message;

    private Fatals(String message) {
        this.message = message;
    }

    @Override
    public SyntaxErrorLevel level() {
        return SyntaxErrorLevel.Fatal;
    }

    @Override
    public String message() {
        return this.message;
    }
}
