package com.github.nuclearg.nagisa.lang.ast;

/**
 * 标识符信息
 * 
 * @author ng
 *
 */
public final class IdentifierInfo {
    /**
     * 标识符类型
     */
    private final IdentifierType type;
    /**
     * 关联的UDT的名称，如果和UDT无关则为null
     */
    private final String udtTypeName;

    public IdentifierInfo(IdentifierType type, String udtTypeName) {
        this.type = type;
        this.udtTypeName = udtTypeName;
    }

    /** 标识符类型 */
    public IdentifierType getType() {
        return this.type;
    }

    /** 关联的UDT的名称，如果和UDT无关则为null */
    public String getUdtTypeName() {
        return this.udtTypeName;
    }

}
