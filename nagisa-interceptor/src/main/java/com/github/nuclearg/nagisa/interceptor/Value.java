package com.github.nuclearg.nagisa.interceptor;

import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;

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
    private final TypeIdentifierInfo type;
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
        this.type = TypeIdentifierInfo.INTEGER;
        this.intValue = value;
        this.strValue = null;
        this.boolValue = false;
    }

    Value(String value) {
        this.type = TypeIdentifierInfo.STRING;
        this.intValue = 0;
        this.strValue = value;
        this.boolValue = false;
    }

    Value(boolean value) {
        this.type = TypeIdentifierInfo.BOOLEAN;
        this.intValue = 0;
        this.strValue = null;
        this.boolValue = value;
    }

    /** 值的类型 */
    public TypeIdentifierInfo getType() {
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
        if (this.type == TypeIdentifierInfo.INTEGER)
            return "[I]" + this.intValue;
        if (this.type == TypeIdentifierInfo.STRING)
            return "[S]" + this.strValue;
        if (this.type == TypeIdentifierInfo.BOOLEAN)
            return "[B]" + this.boolValue;

        return "[" + this.type + "]UNKNOWN";
    }
}
