package com.github.nuclearg.nagisa.lang.lexer;

import com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition.NagisaLexTokenType;
import com.github.nuclearg.nagisa.lang.util.Range;

/**
 * 词法元素
 * 
 * @author enji.lj
 *
 */
public class LexToken {
    /**
     * 词法元素类型
     */
    public final LexTokenType type;
    /**
     * 对应的实际文本
     */
    public final String text;
    /**
     * 在源文件中的位置
     */
    public final Range range;

    public LexToken(LexTokenType type, String text, Range range) {
        this.type = type;
        this.text = text;
        this.range = range;
    }

    @Override
    public String toString() {
        if (this.type == NagisaLexTokenType.EOL)
            return "[EOL]";
        else
            return this.text + " [" + this.type + "]";
    }
}
