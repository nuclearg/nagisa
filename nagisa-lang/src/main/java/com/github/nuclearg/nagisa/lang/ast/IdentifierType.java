package com.github.nuclearg.nagisa.lang.ast;

/**
 * 标识符类型
 * 
 * @author ng
 *
 */
public enum IdentifierType {
    /**
     * 整数变量名
     */
    IntegerVariable,
    /**
     * 字符串变量名
     */
    StringVariable,
    /**
     * 布尔变量名
     */
    BooleanVariable,
    /**
     * 自定义类型变量名
     */
    UDTVariable,

    /**
     * 自定义类型名
     */
    UDT,

    /**
     * 整数函数名
     */
    IntegerFunction,
    /**
     * 字符串函数名
     */
    StringFunction,
    /**
     * 布尔函数名
     */
    BooleanFunction,
    /**
     * 返回自定义类型的函数名
     */
    UDTFunction,
    /**
     * 方法名
     */
    VoidFunction,
}
