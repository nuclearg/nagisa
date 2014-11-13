package com.github.nuclearg.nagisa.lang.ast;

/**
 * 标识符注册表
 * 
 * @author ng
 *
 */
public interface IdentifierRegistry {

    /**
     * 获取标识符信息
     * 
     * @param name 
     * @return
     */
    public IdentifierInfo getIdentifier(String name);
}
