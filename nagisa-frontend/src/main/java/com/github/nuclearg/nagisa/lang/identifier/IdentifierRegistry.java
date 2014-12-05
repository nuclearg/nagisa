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
        if (this.variableMap.containsKey(name.toUpperCase())) {
            VariableIdentifierInfo info = this.variableMap.get(name.toUpperCase());
            if (info.getType() != type)
                errorReporter.report(Errors.E1001, position, name, info.getType(), type);
        } else
            this.variableMap.put(name.toUpperCase(), new VariableIdentifierInfo(name, type));
    }

    /**
     * 查询一个变量
     * 
     * @param name
     *            变量名
     * @param position
     *            当前位置
     * @return 变量信息，可能为null
     */
    public VariableIdentifierInfo queryVariableInfo(String name, Position position) {
        return this.variableMap.get(name);
    }

    /**
     * 查询一个期望类型的变量
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
     * 查询一个函数
     * 
     * @param name
     *            函数名
     * @param position
     *            当前位置
     * @return 函数信息，可能为null
     */
    public FunctionIdentifierInfo queryFunctionInfo(String name, Position position) {
        return this.functionMap.get(name);
    }

    /**
     * 查询一个类型
     * 
     * @param name
     *            类型名
     * @param position
     *            当前位置
     * @return 类型，可能为null
     */
    public IdentifierType queryTypeInfo(String name, Position position) {
        IdentifierType type = this.typeMap.get(name.toUpperCase());

        if (type == IdentifierType.VOID) {
            this.errorReporter.report(Errors.E1007, position);
            type = null;
        }

        return type;
    }
}
