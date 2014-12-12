package com.github.nuclearg.nagisa.interceptor;

import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;
import com.github.nuclearg.nagisa.interceptor.ex.NagisaInterceptorInternalException;

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
     * 值
     */
    private final Object value;

    Value(Object value, TypeIdentifierInfo type) {
        if (type == null)
            throw new NagisaInterceptorInternalException("value.type is null");
        if (value == null)
            throw new NagisaInterceptorInternalException("value.value is null");

        this.type = type;
        this.value = value;
    }

    Value(long value) {
        this(value, TypeIdentifierInfo.INTEGER);
    }

    Value(String value) {
        this(value, TypeIdentifierInfo.STRING);
    }

    Value(boolean value) {
        this(value, TypeIdentifierInfo.BOOLEAN);
    }

    /** 值的类型 */
    public TypeIdentifierInfo getType() {
        return this.type;
    }

    /** 值 */
    public Object getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        if (this.type == TypeIdentifierInfo.INTEGER)
            return "[I]" + this.value;
        if (this.type == TypeIdentifierInfo.STRING)
            return "[S]" + this.value;
        if (this.type == TypeIdentifierInfo.BOOLEAN)
            return "[B]" + this.value;

        return "[" + this.type + "]UNKNOWN";
    }
}
