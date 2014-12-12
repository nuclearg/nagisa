package com.github.nuclearg.nagisa.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;
import com.github.nuclearg.nagisa.interceptor.ex.NagisaCodeRuntimeException;

/**
 * 环境上下文
 * 
 * @author ng
 *
 */
final class Context {
    /**
     * 父上下文，用于支持函数
     * <p>
     * 子上下文仅将继承父上下文的函数，不会继承任何变量
     * </p>
     */
    private final Context parent;

    /**
     * 变量表
     */
    private final Map<String, Value> variables = new HashMap<>();
    /**
     * 函数表
     */
    private final Map<String, FunctionInterceptor> functions = new HashMap<>();

    Context() {
        this(null);
    }

    Context(Context parent) {
        this.parent = parent;
    }

    /**
     * 获取整形变量的值
     * 
     * @param name
     *            变量名
     * @return 变量的值
     */
    long getIntegerVariableValue(String name) {
        return (Long) this.getVariableValue(name, TypeIdentifierInfo.INTEGER);
    }

    /**
     * 获取字符串类类形变量的值
     * 
     * @param name
     *            变量名
     * @return 变量的值
     */
    String getStringVariableValue(String name) {
        return (String) this.getVariableValue(name, TypeIdentifierInfo.INTEGER);
    }

    /**
     * 获取布尔变量的值
     * 
     * @param name
     *            变量名
     * @return 变量的值
     */
    boolean getBooleanVariableValue(String name) {
        return (Boolean) this.getVariableValue(name, TypeIdentifierInfo.INTEGER);
    }

    /**
     * 获取变量的值
     * 
     * @param name
     *            变量名
     * @return 变量的值
     */
    Value getVariableValue(String name) {
        if (!this.variables.containsKey(name))
            throw new NagisaCodeRuntimeException("变量 " + name + " 不存在");

        return this.variables.get(name);
    }

    /**
     * 获取变量的值
     * 
     * @param name
     *            变量名
     * @param type
     *            期望的类型
     * @return 变量的值
     */
    Object getVariableValue(String name, TypeIdentifierInfo type) {
        Value value = this.getVariableValue(name);
        if (value.getType() != type)
            throw new NagisaCodeRuntimeException("变量 " + name + " 的类型不匹配。期望类型 " + type + " 但实际类型是 " + value.getType());

        return value.getValue();
    }

    /**
     * 判断指定的变量是否存在
     * 
     * @param name
     *            变量名
     * @return 变量是否存在
     */
    boolean isVariableExists(String name) {
        return this.variables.containsKey(name);
    }

    /**
     * 判断指定的变量是否存在，并且类型与指定的类型一致
     * 
     * @param name
     *            变量名
     * @param type
     *            期望的类型
     * @return 变量是否存在且类型一致
     */
    boolean isVariableExists(String name, TypeIdentifierInfo type) {
        Value value = this.getVariableValue(name);
        if (value == null)
            return false;

        return value.getType() == type;
    }

    /**
     * 设置变量的新值
     * 
     * @param name
     *            变量名
     * @param value
     *            变量的新值
     */
    void setVariableValue(String name, Value value) {
        Value oldValue = this.variables.get(name);
        if (oldValue != null && oldValue.getType() != value.getType())
            throw new NagisaCodeRuntimeException("尝试向变量 " + name + " 设置一个不同类型的新值。原始类型 " + oldValue.getType() + " 但新的值是 " + value.getValue() + "，其类型是 " + value.getType());

        this.variables.put(name, value);
    }

    /**
     * 从上下文中清除变量
     * 
     * @param name
     *            变量名
     */
    void clearVariable(String name) {
        this.variables.remove(name);
    }

    /**
     * 向上下文中注册一个方法
     * 
     * @param function
     *            方法的解释器
     */
    void registerFunction(FunctionInterceptor function) {
        this.functions.put(function.getName(), function);
    }

    /**
     * 从上下文中获取一个方法
     * 
     * @param name
     *            方法名
     * @return 方法的解释器
     */
    FunctionInterceptor getFunction(String name) {
        if (this.parent != null) // 方法信息首先从父上下文中找
            return this.parent.getFunction(name);

        if (!this.functions.containsKey(name))
            throw new NagisaCodeRuntimeException("函数或方法不存在. name: " + name);

        return this.functions.get(name);
    }

    /**
     * 调用函数
     * 
     * @param name
     *            函数名
     * @param values
     *            实参列表
     * @return 返回值
     */
    Value invokeFunction(String name, List<Value> values) {
        // 找到函数的解释器
        FunctionInterceptor function = this.getFunction(name);

        // 创建一个新的上下文
        Context ctx = new Context(this);

        // 参数数量压栈
        ctx.setVariableValue("$ARG_COUNT", new Value(values.size()));
        // 参数压栈
        for (int i = 0; i < values.size(); i++)
            ctx.setVariableValue("$ARG_" + i, values.get(i));

        // 调用
        function.eval(ctx);

        // 从上下文中取回返回值
        if (ctx.isVariableExists("$RET_VALUE"))
            return ctx.getVariableValue("$RET_VALUE");
        else
            return null;
    }
}
