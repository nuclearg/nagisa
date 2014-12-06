package com.github.nuclearg.nagisa.frontend.identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 标识符的类型
 * 
 * @author ng
 *
 */
public final class TypeIdentifierInfo {
    /**
     * 整型
     */
    public static final TypeIdentifierInfo INTEGER = new TypeIdentifierInfo("INTEGER");
    /**
     * 字符串
     */
    public static final TypeIdentifierInfo STRING = new TypeIdentifierInfo("STRING");
    /**
     * 布尔
     */
    public static final TypeIdentifierInfo BOOLEAN = new TypeIdentifierInfo("BOOLEAN");
    /**
     * 字符串
     */
    public static final TypeIdentifierInfo VOID = new TypeIdentifierInfo("VOID");

    /**
     * 基础类型列表
     */
    public static final Map<String, TypeIdentifierInfo> NAGISA_BASE_TYPES;

    static {
        Map<String, TypeIdentifierInfo> types = new HashMap<>();

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

    public TypeIdentifierInfo(String name) {
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
