package com.github.nuclearg.nagisa.frontend.identifier;

/**
 * 变量信息
 * 
 * @author ng
 *
 */
public final class VariableIdentifierInfo {
    /**
     * 变量名
     */
    private final String name;
    /**
     * 类型
     */
    private final TypeIdentifierInfo type;

    public VariableIdentifierInfo(String name, TypeIdentifierInfo type) {
        this.name = name;
        this.type = type;
    }

    /** 变量名 */
    public String getName() {
        return this.name;
    }

    /** 类型 */
    public TypeIdentifierInfo getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.name + " AS " + this.type;
    }
}
