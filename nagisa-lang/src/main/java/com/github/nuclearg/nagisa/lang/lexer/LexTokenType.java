package com.github.nuclearg.nagisa.lang.lexer;

import java.util.regex.Pattern;

/**
 * 定义词法元素的类型
 * 
 * @author ng
 *
 */
public interface LexTokenType {
    /**
     * 词法元素的名称
     */
    public String name();

    /**
     * 词法元素的正则表达式
     */
    public Pattern regex();

    /**
     * 在词法分析的输出中是否直接忽略掉
     */
    public boolean transparent();
}
