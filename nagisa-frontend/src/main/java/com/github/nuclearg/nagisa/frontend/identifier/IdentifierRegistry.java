package com.github.nuclearg.nagisa.frontend.identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 标识符注册表
 * 
 * @author ng
 *
 */
public class IdentifierRegistry {
    /**
     * 变量标识符注册表
     */
    private final Map<String, VariableIdentifierInfo> variableMap = new HashMap<>();
    /**
     * 函数标识符注册表
     */
    private final Map<String, FunctionIdentifierInfo> functionMap = new HashMap<>();
    /**
     * 类型标识符列表
     */
    private final Map<String, IdentifierType> typeMap = new HashMap<>(IdentifierType.NAGISA_BASE_TYPES);

    /**
     * 错误报告器
     */
    private final SyntaxErrorReporter errorReporter;

    public IdentifierRegistry(SyntaxErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    /**
     * 查询一个变量
     * 
     * @param name
     *            变量名
     * @return 变量信息，可能为null
     */
    public VariableIdentifierInfo queryVariableInfo(String name) {
        return this.variableMap.get(name.toUpperCase());
    }

    /**
     * 注册一个变量
     * 
     * @param name
     *            变量名
     * @param type
     *            变量类型
     * @param position
     *            当前位置
     */
    public void registerVariableInfo(String name, IdentifierType type, SyntaxTreeNode node) {
        if (this.variableMap.containsKey(name.toUpperCase())) {
            VariableIdentifierInfo info = this.variableMap.get(name.toUpperCase());
            if (info.getType() != type)
                errorReporter.report(node, Errors.E1002, name, info.getType(), type);
        } else
            this.variableMap.put(name.toUpperCase(), new VariableIdentifierInfo(name, type));
    }

    /**
     * 查询一个函数或方法
     * 
     * @param name
     *            函数名或方法名
     * @return 函数或方法的信息，可能为null
     */
    public FunctionIdentifierInfo queryFunctionInfo(String name) {
        return this.functionMap.get(name.toUpperCase());
    }

    /**
     * 注册一个函数
     * 
     * @param name
     *            函数名
     * @param type
     *            返回类型
     * @param parameters
     *            参数列表
     * @parma node 当前语法树节点
     */
    public void registerFunctionInfo(String name, IdentifierType type, List<VariableIdentifierInfo> parameters, SyntaxTreeNode node) {
        if (this.functionMap.containsKey(name.toUpperCase()))
            errorReporter.report(node, Errors.E1003, name);
        else
            this.functionMap.put(name, new FunctionIdentifierInfo(name, type, parameters));
    }

    /**
     * 查询一个类型
     * 
     * @param name
     *            类型名
     * @return 类型，可能为null
     */
    public IdentifierType queryTypeInfo(String name) {
        IdentifierType type = this.typeMap.get(name.toUpperCase());

        return type;
    }
}
