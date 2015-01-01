package com.github.nuclearg.nagisa.frontend.symbol;

/**
 * 变量符号
 * 
 * @author ng
 *
 */
public final class VariableSymbol {
    /**
     * 变量名
     */
    private final String name;
    /**
     * 类型
     */
    private final TypeSymbol type;

    public VariableSymbol(String name, TypeSymbol type) {
        this.name = name;
        this.type = type;
    }

    /** 变量名 */
    public String getName() {
        return this.name;
    }

    /** 类型 */
    public TypeSymbol getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.name + " AS " + this.type;
    }

}
