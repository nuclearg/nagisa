package com.github.nuclearg.nagisa.frontend.symbol;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型符号
 * 
 * @author ng
 *
 */
public final class TypeSymbol {
    /**
     * 整型
     */
    public static final TypeSymbol INTEGER = new TypeSymbol("INTEGER");
    /**
     * 字符串
     */
    public static final TypeSymbol STRING = new TypeSymbol("STRING");
    /**
     * 布尔
     */
    public static final TypeSymbol BOOLEAN = new TypeSymbol("BOOLEAN");
    /**
     * 字符串
     */
    public static final TypeSymbol VOID = new TypeSymbol("VOID");

    /**
     * 基础类型列表
     */
    public static final Map<String, TypeSymbol> NAGISA_BASE_TYPES;

    static {
        Map<String, TypeSymbol> types = new HashMap<>();

        types.put(INTEGER.name, INTEGER);
        types.put(STRING.name, STRING);
        types.put(BOOLEAN.name, BOOLEAN);
        types.put(VOID.name, VOID);

        NAGISA_BASE_TYPES = Collections.unmodifiableMap(types);
    }

    /**
     * 类型名称
     */
    private final String name;

    public TypeSymbol(String name) {
        this.name = name;
    }

    /** 类型名称 */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
