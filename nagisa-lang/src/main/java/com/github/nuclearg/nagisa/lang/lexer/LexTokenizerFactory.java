package com.github.nuclearg.nagisa.lang.lexer;

/**
 * {@link LexTokenizer}的工厂类
 * 
 * @author ng
 *
 */
public final class LexTokenizerFactory {
    public static LexTokenizer buildLexTokenizer(LexDefinition definition, String text) {
        return new LexTokenizer(definition, text);
    }
}
