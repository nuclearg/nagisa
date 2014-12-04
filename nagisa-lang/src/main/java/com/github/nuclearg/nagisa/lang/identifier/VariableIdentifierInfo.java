package com.github.nuclearg.nagisa.lang.identifier;

/**
 * 变量信息
 * 
 * @author ng
 *
 */
public class VariableIdentifierInfo {
    /**
     * 变量名
     */
    private final String name;
    /**
     * 类型
     */
    private final IdentifierType type;

    public VariableIdentifierInfo(String name, IdentifierType type) {
        this.name = name;
        this.type = type;
    }

    /** 变量名 */
    public String getName() {
        return this.name;
    }

    /** 类型 */
    public IdentifierType getType() {
        return this.type;
    }

}
