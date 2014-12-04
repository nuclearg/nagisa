package com.github.nuclearg.nagisa.lang.identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.nuclearg.nagisa.lang.error.Errors;
import com.github.nuclearg.nagisa.lang.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.lexer.Position;

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
     * 错误报告器
     */
    private final SyntaxErrorReporter errorReporter;

    public IdentifierRegistry(SyntaxErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
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
    public void registerVariableInfo(String name, IdentifierType type, Position position) {
        if (this.variableMap.containsKey(name.toUpperCase()))
            errorReporter.report(Errors.E1001, position, name);
        else
            this.variableMap.put(name.toUpperCase(), new VariableIdentifierInfo(name, type));
    }

    /**
     * 获取一个变量
     * 
     * @param name
     *            变量名
     * @param position
     *            当前位置
     * @return 变量信息，可能为null
     */
    public VariableIdentifierInfo queryVariableInfo(String name, Position position) {
        if (!this.variableMap.containsKey(name.toUpperCase()))
            errorReporter.report(Errors.E1002, position, name);

        return this.variableMap.get(name);
    }

    /**
     * 获取一个期望类型的变量
     * 
     * @param name
     *            变量名
     * @param position
     *            当前位置
     * @param expectedType
     *            期望的类型
     * @return 变量信息，可能为null
     */
    public VariableIdentifierInfo queryVariableInfo(String name, Position position, IdentifierType expectedType) {
        VariableIdentifierInfo info = this.queryVariableInfo(name, position);
        if (info == null)
            return null;

        if (info.getType() != expectedType) {
            errorReporter.report(Errors.E1003, position, name, expectedType, info.getType());
            return null;
        }

        return info;
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
     * @parma position 当前位置
     */
    public void registerFunctionInfo(String name, IdentifierType type, List<VariableIdentifierInfo> parameters, Position position) {
        if (this.functionMap.containsKey(name.toUpperCase()))
            errorReporter.report(Errors.E2001, position, name);
        else
            this.functionMap.put(name, new FunctionIdentifierInfo(name, type, parameters));
    }

    /**
     * 获取一个函数
     * 
     * @param name
     *            函数名
     * @param position
     *            当前位置
     * @return 函数信息，可能为null
     */
    public FunctionIdentifierInfo queryFunctionInfo(String name, Position position) {
        if (!this.functionMap.containsKey(name.toUpperCase()))
            errorReporter.report(Errors.E2002, position, name);

        return this.functionMap.get(name);
    }

}
