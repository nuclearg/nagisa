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
    private final Map<String, Long> integerMap = new HashMap<>();
    private final Map<String, String> stringMap = new HashMap<>();

    long getIntegerValue(String name) {
        if (this.integerMap.get(name) == null)
            this.integerMap.put(name, 0L);

        return this.integerMap.get(name);
    }

    void setIntegerValue(String name, long value) {
        this.integerMap.put(name, value);
    }

    String getStringValue(String name) {
        if (this.stringMap.get(name) == null)
            this.stringMap.put(name, "");

        return this.stringMap.get(name);
    }

    void setStringValue(String name, String value) {
        this.stringMap.put(name, value);
    }

    void setValue(String name, Value value) {
        switch (value.getType()) {
            case Integer:
                setIntegerValue(name, value.getIntegerValue());
                break;
            case String:
                setStringValue(name, value.getStringValue());
                break;
            default:
                throw new UnsupportedOperationException("value: " + value);
        }
    }
}
