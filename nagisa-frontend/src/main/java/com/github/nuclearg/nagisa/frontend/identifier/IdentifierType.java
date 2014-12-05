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
public final class IdentifierType {
    /**
     * 整型
     */
    public static final IdentifierType INTEGER = new IdentifierType("INTEGER");
    /**
     * 字符串
     */
    public static final IdentifierType STRING = new IdentifierType("STRING");
    /**
     * 布尔
     */
    public static final IdentifierType BOOLEAN = new IdentifierType("BOOLEAN");
    /**
     * 字符串
     */
    public static final IdentifierType VOID = new IdentifierType("VOID");

    /**
     * 基础类型列表
     */
    public static final Map<String, IdentifierType> NAGISA_BASE_TYPES;

    static {
        Map<String, IdentifierType> types = new HashMap<>();

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

    public IdentifierType(String name) {
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
