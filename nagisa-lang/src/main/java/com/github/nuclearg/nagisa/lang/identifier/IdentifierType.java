package com.github.nuclearg.nagisa.lang.identifier;

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
