package com.github.nuclearg.nagisa.frontend.symbol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 符号注册表
 * 
 * @author ng
 *
 */
public final class SymbolRegistry {
    /**
     * 变量符号注册表
     */
    private final Map<String, VariableSymbol> variableMap = new HashMap<>();
    /**
     * 函数符号注册表
     */
    private final Map<String, FunctionSymbol> functionMap = new HashMap<>();
    /**
     * 类型符号列表
     */
    private final Map<String, TypeSymbol> typeMap = new HashMap<>(TypeSymbol.NAGISA_BASE_TYPES);

    /**
     * 错误报告器
     */
    private final SyntaxErrorReporter errorReporter;

    public SymbolRegistry(SyntaxErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    public SymbolRegistry(SymbolRegistry registry, boolean copyVariables) {
        this.errorReporter = registry.errorReporter;

        if (copyVariables)
            this.variableMap.putAll(this.variableMap);
        this.functionMap.putAll(this.functionMap);
        this.typeMap.putAll(this.typeMap);
    }

    /**
     * 注册一个变量符号
     * 
     * @param name
     *            变量名
     * @param type
     *            变量类型
     * @param position
     *            当前位置
     */
    public void registerVariableInfo(String name, TypeSymbol type, SyntaxTreeNode node) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("variable name is blank");
        if (type == null)
            throw new IllegalArgumentException("variable type is null");
        if (node == null)
            throw new IllegalArgumentException("variable node is null");

        // 判断是否重复
        if (this.variableMap.containsKey(name.toUpperCase())) {
            VariableSymbol variable = this.variableMap.get(name.toUpperCase());
            if (variable.getType() != type) {
                errorReporter.report(node, Errors.E1002, name, variable.getType(), type);
                return;
            }
        }

        // 不能为 VOID 类型
        if (type == TypeSymbol.VOID)
            this.errorReporter.report(node, Errors.E1104, name);

        // 把变量加到注册表中
        this.variableMap.put(name.toUpperCase(), new VariableSymbol(name, type));
    }

    /**
     * 查询一个变量符号
     * 
     * @param name
     *            变量名
     * @param node
     *            引用这个变量的语法节点
     * @return 变量信息，可能为null
     */
    public VariableSymbol lookupVariableSymbol(String name, SyntaxTreeNode node) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("variable ref name is blank");
        if (node == null)
            throw new IllegalArgumentException("variable ref node is null");

        // 变量必须存在
        if (!this.variableMap.containsKey(name.toUpperCase())) {
            errorReporter.report(node, Errors.E1001, name);
            return null;
        }

        return this.variableMap.get(name.toUpperCase());
    }

    /**
     * 注册一个函数符号
     * 
     * @param name
     *            函数名
     * @param type
     *            返回类型
     * @param parameters
     *            参数列表
     * @param isFunction
     *            true表示注册的是一个函数，false表示注册的是一个方法
     * @parma node 当前语法树节点
     */
    public void registerFunctionInfo(String name, TypeSymbol type, List<VariableSymbol> parameters, boolean isFunction, SyntaxTreeNode node) {
        // 判空
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("function name is blank");
        if (type == null)
            throw new IllegalArgumentException("function type is null");
        if (parameters == null)
            throw new IllegalArgumentException("function parameters is null");
        if (node == null)
            throw new IllegalArgumentException("function node is null");

        // 判断是否重复
        if (this.functionMap.containsKey(name.toUpperCase())) {
            errorReporter.report(node, Errors.E1004, name);
            return;
        }

        // 函数的返回类型不能为 VOID
        if (isFunction && type == TypeSymbol.VOID)
            errorReporter.report(node, Errors.E1105, name);

        // 把函数加到注册表中
        FunctionSymbol function = new FunctionSymbol(name, type, parameters);
        this.functionMap.put(name.toUpperCase(), function);
    }

    /**
     * 查询一个函数符号
     * 
     * @param name
     *            函数名
     * @param parameters
     *            期望参数类型列表
     * @param isFunction
     *            true表示查询的是一个函数，false表示查询的是一个方法
     * @param node
     *            引用函数的语法节点
     * @return 函数符号，或为null
     */
    public FunctionSymbol lookupFunctionSymbol(String name, List<TypeSymbol> parameterTypes, boolean isFunction, SyntaxTreeNode node) {
        // 判空
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("function ref name is blank");
        if (parameterTypes == null)
            throw new IllegalArgumentException("function expected parameter types is null");
        if (node == null)
            throw new IllegalArgumentException("function ref node is null");

        // 函数必须存在
        if (!this.functionMap.containsKey(name.toUpperCase())) {
            this.errorReporter.report(node, Errors.E1003, name);
            return null;
        }

        FunctionSymbol function = this.functionMap.get(name.toUpperCase());

        boolean error = false;

        // 不能以表达式的方式调用方法 （但可以以调用方法的形式调用函数）
        if (isFunction && function.getType() == TypeSymbol.VOID) {
            this.errorReporter.report(node, Errors.E2003, function.getName());
            error = true;
        }
        // 检查形参和实参的数量是否匹配
        if (parameterTypes.size() != function.getParameters().size()) {
            this.errorReporter.report(node, Errors.E2004, function.getName(), function.getParameters().size(), parameterTypes.size());
            error = true;
        }
        // 检查形参和实参的类型是否匹配
        for (int i = 0; i < parameterTypes.size(); i++)
            if (parameterTypes.get(i) != function.getParameters().get(i).getType()) {
                errorReporter.report(node, Errors.E2005, function.getName(), i, function.getParameters().get(i).getName(), function.getParameters().get(i).getType(), parameterTypes.get(i));
                error = true;
            }

        if (error)
            return null;

        return function;
    }

    /**
     * 查询一个类型符号
     * 
     * @param name
     *            类型名称
     * @param node
     *            引用类型的语法节点
     * @return 类型符号，或为null
     */
    public TypeSymbol lookupTypeSymbol(String name, SyntaxTreeNode node) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("type ref name is blank");
        if (node == null)
            throw new IllegalArgumentException("type ref node is null");

        // 类型必须存在
        if (!this.typeMap.containsKey(name.toUpperCase())) {
            this.errorReporter.report(node, Errors.E1005, name);
            return null;
        }

        return this.typeMap.get(name.toUpperCase());
    }
}
