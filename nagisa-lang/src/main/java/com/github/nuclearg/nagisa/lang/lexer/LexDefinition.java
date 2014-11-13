package com.github.nuclearg.nagisa.lang.lexer;

import java.util.Collections;
import java.util.List;

/**
 * 一篇完整的词法元素定义
 * 
 * @author ng
 *
 */
abstract class LexDefinition {
    /**
     * 词法定义列表
     */
    private final List<LexTokenType> types;

    protected LexDefinition(List<LexTokenType> types) {
        this.types = Collections.unmodifiableList(types);
    }

    /** 词法定义列表 */
    final List<LexTokenType> getTypes() {
        return this.types;
    }

    @Override
    public String toString() {
        return this.types.toString();
    }
}
