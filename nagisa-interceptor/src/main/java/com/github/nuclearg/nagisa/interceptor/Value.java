package com.github.nuclearg.nagisa.interceptor;

import com.github.nuclearg.nagisa.lang.ast.ExprType;

/**
 * 解释器中各个表达式运算出的值
 * 
 * @author ng
 *
 */
class Value {
    /**
     * 值的类型
     */
    private final ExprType type;
    /**
     * 数字值
     */
    private final long intValue;
    /**
     * 字符串值
     */
    private final String strValue;
    /**
     * 布尔值
     */
    private final boolean boolValue;

    Value(long value) {
        this.type = ExprType.Integer;
        this.intValue = value;
        this.strValue = null;
        this.boolValue = false;
    }

    Value(String value) {
        this.type = ExprType.String;
        this.intValue = 0;
        this.strValue = value;
        this.boolValue = false;
    }

    Value(boolean value) {
        this.type = ExprType.Boolean;
        this.intValue = 0;
        this.strValue = null;
        this.boolValue = value;
    }

    /** 值的类型 */
    public ExprType getType() {
        return this.type;
    }

    /** 数字值 */
    public long getIntegerValue() {
        return this.intValue;
    }

    /** 字符串值 */
    public String getStringValue() {
        return this.strValue;
    }

    /** 布尔值 */
    public boolean getBooleanValue() {
        return this.boolValue;
    }

    @Override
    public String toString() {
        switch (this.type) {
            case Integer:
                return "[I]" + this.intValue;
            case String:
                return "[S]" + this.strValue;
            case Boolean:
                return "[B]" + this.boolValue;
            default:
                return "[" + this.type + "]UNKNOWN";
        }
    }
}
