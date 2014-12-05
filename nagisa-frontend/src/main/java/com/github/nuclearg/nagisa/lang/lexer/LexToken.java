package com.github.nuclearg.nagisa.lang.lexer;

import com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition.NagisaLexTokenType;
import com.github.nuclearg.nagisa.lang.util.Range;

/**
 * 词法元素
 * 
 * @author enji.lj
 *
 */
public final class LexToken {
    /**
     * 词法元素类型
     */
    private final LexTokenType type;
    /**
     * 对应的实际文本
     */
    private final String text;
    /**
     * 在源文件中的位置
     */
    private final Range range;

    LexToken(LexTokenType type, String text, Range range) {
        this.type = type;
        this.text = text;
        this.range = range;
    }

    /** 词法元素类型 */
    public LexTokenType getType() {
        return this.type;
    }

    /** 对应的实际文本 */
    public String getText() {
        return this.text;
    }

    /** 在源文件中的位置 */
    public Range getRange() {
        return this.range;
    }

    @Override
    public String toString() {
        if (this.type == NagisaLexTokenType.EOL)
            return "[EOL]";
        else
            return this.text + "[" + this.type + "]";
    }
}
