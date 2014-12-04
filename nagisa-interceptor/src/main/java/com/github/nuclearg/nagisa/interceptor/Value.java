package com.github.nuclearg.nagisa.interceptor;

import com.github.nuclearg.nagisa.lang.identifier.IdentifierType;

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
    private final IdentifierType type;
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
        this.type = IdentifierType.INTEGER;
        this.intValue = value;
        this.strValue = null;
        this.boolValue = false;
    }

    Value(String value) {
        this.type = IdentifierType.STRING;
        this.intValue = 0;
        this.strValue = value;
        this.boolValue = false;
    }

    Value(boolean value) {
        this.type = IdentifierType.BOOLEAN;
        this.intValue = 0;
        this.strValue = null;
        this.boolValue = value;
    }

    /** 值的类型 */
    public IdentifierType getType() {
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
        if (this.type == IdentifierType.INTEGER)
            return "[I]" + this.intValue;
        if (this.type == IdentifierType.STRING)
            return "[S]" + this.strValue;
        if (this.type == IdentifierType.BOOLEAN)
            return "[B]" + this.boolValue;

        return "[" + this.type + "]UNKNOWN";
    }
}
