package com.github.nuclearg.nagisa.lang.error;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.lexer.Position;

/**
 * 错误记录
 * 
 * @author ng
 *
 */
public final class SyntaxErrorLogItem {
    /**
     * 错误类型
     */
    private final SyntaxErrorType error;
    /**
     * 参数
     */
    private final Object[] args;
    /**
     * 发生错误的位置
     */
    private final Position position;
    /**
     * 错误内容
     */
    private final String message;

    SyntaxErrorLogItem(SyntaxErrorType error, Object[] args, Position position) {
        this.error = error;
        this.args = args;
        this.position = position;
        this.message = String.format(Locale.getDefault(), error.message(), args);
    }

    /** 错误类型 */
    public SyntaxErrorType getError() {
        return this.error;
    }

    /** 参数 */
    public Object[] getArgs() {
        return this.args;
    }

    /** 发生错误的位置 */
    public Position getPosition() {
        return this.position;
    }

    /** 错误内容 */
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        String level = StringUtils.rightPad(this.error.level().name().toUpperCase(), 5, ' ');

        return level + " " + this.position + " " + this.error.name() + ": " + this.message;
    }

}
