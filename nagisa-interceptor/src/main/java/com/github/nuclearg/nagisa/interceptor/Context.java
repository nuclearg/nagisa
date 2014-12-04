package com.github.nuclearg.nagisa.interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 环境上下文
 * 
 * @author ng
 *
 */
class Context {
    /**
     * 运行时错误报告器
     */
    private final RuntimeErrorReporter errorReporter;
    /**
     * 变量表
     */
    private final Map<String, Value> variableMap = new HashMap<>();

    Context(RuntimeErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    long getIntegerVariableValue(String name) {
        if (!this.variableMap.containsKey(name)) {
            errorReporter.report("变量不存在");
            return 0;
        }

        return this.variableMap.get(name).getIntegerValue();
    }

    String getStringVariableValue(String name) {
        if (!this.variableMap.containsKey(name)) {
            errorReporter.report("变量不存在");
            return "";
        }

        return this.variableMap.get(name).getStringValue();
    }

    boolean getBooleanVariableValue(String name) {
        if (!this.variableMap.containsKey(name)) {
            errorReporter.report("变量不存在");
            return false;
        }

        return this.variableMap.get(name).getBooleanValue();
    }

    void setVariableValue(String name, Value value) {
        this.variableMap.put(name, value);
    }
}
