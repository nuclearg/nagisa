package com.github.nuclearg.nagisa.lang.ast;

/**
 * 表达式类型
 * 
 * @author ng
 *
 */
public enum ExprOperator {
    /** 整数字面量 */
    IntegerLiteral,
    /** 字符串字面量 */
    StringLiteral,
    /** 整数变量引用 */
    IntegerVariableRef,
    /** 字符串变量引用 */
    StringVariableRef,

    /** 整数取负 */
    IntegerNegative,

    /** 整数加 */
    IntegerAdd,
    /** 整数减 */
    IntegerSub,
    /** 整数乘 */
    IntegerMul,
    /** 整数除 */
    IntegerDiv,
    /** 整数求余 */
    IntegerMod,

    /** 整数相等 */
    IntegerEq,
    /** 整数不相等 */
    IntegerNeq,
    /** 整数大于 */
    IntegerGt,
    /** 整数大于等于 */
    IntegerGte,
    /** 整数小于 */
    IntegerLt,
    /** 整数小于等于 */
    IntegerLte,

    /** 字符串加 */
    StringAdd,

    /** 字符串相等 */
    StringEq,
    /** 字符串不相等 */
    StringNeq,
    /** 字符串大于 */
    StringGt,
    /** 字符串大于等于 */
    StringGte,
    /** 字符串小于 */
    StringLt,
    /** 字符串小于等于 */
    StringLte,

    /** 逻辑取反 */
    BooleanNot,
    /** 逻辑与 */
    BooleanAnd,
    /** 逻辑或 */
    BooleanOr,
    /** 逻辑异或 */
    BooleanXor,
}
