package com.github.nuclearg.nagisa.frontend.identifier;

import java.util.Collections;
import java.util.List;

/**
 * 函数信息
 * 
 * @author ng
 *
 */
public class FunctionIdentifierInfo {
    /**
     * 函数名
     */
    private final String name;
    /**
     * 返回类型
     */
    private final IdentifierType type;
    /**
     * 形参列表
     */
    private final List<VariableIdentifierInfo> parameters;

    public FunctionIdentifierInfo(String name, IdentifierType type, List<VariableIdentifierInfo> parameters) {
        this.name = name;
        this.type = type;
        this.parameters = Collections.unmodifiableList(parameters);
    }

    /** 函数名 */
    public String getName() {
        return this.name;
    }

    /** 返回类型 */
    public IdentifierType getType() {
        return this.type;
    }

    /** 形参列表 */
    public List<VariableIdentifierInfo> getParameters() {
        return this.parameters;
    }

}
