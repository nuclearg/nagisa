package com.github.nuclearg.nagisa.frontend.symbol;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 函数符号
 * 
 * @author ng
 *
 */
public final class FunctionSymbol {
    /**
     * 函数名
     */
    private final String name;
    /**
     * 返回类型
     */
    private final TypeSymbol type;
    /**
     * 形参列表
     */
    private final List<VariableSymbol> parameters;

    public FunctionSymbol(String name, TypeSymbol type, List<VariableSymbol> parameters) {
        this.name = name;
        this.type = type;
        this.parameters = Collections.unmodifiableList(parameters);
    }

    /** 函数名 */
    public String getName() {
        return this.name;
    }

    /** 返回类型 */
    public TypeSymbol getType() {
        return this.type;
    }

    /** 形参列表 */
    public List<VariableSymbol> getParameters() {
        return this.parameters;
    }

    @Override
    public String toString() {
        return "FUNCTION " + this.name + " (" + StringUtils.join(this.parameters, ",") + ") AS " + this.type;
    }
}
