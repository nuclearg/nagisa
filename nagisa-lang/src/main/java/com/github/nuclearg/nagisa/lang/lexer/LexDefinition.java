package com.github.nuclearg.nagisa.lang.lexer;

import java.util.Collections;
import java.util.List;

/**
 * 一篇完整的词法元素定义
 * 
 * @author ng
 *
 */
public class LexDefinition {
    /**
     * 词法定义列表
     */
    public final List<LexTokenType> types;

    public LexDefinition(List<LexTokenType> types) {
        this.types = Collections.unmodifiableList(types);
    }

    @Override
    public String toString() {
        return this.types.toString();
    }
}
