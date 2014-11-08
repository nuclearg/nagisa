package com.github.nuclearg.nagisa.lang.parser;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerSnapshot;

public final class SyntaxErrorReporter {
    private int count;

    public void error(String message, LexTokenizerSnapshot lexerPos) {
        System.err.println(lexerPos + " " + message);

        if (++count >= 20)
            fatal("错误过多，中断解析过程", lexerPos);
    }

    public void fatal(String message, LexTokenizerSnapshot lexerPos) {
        System.err.println(lexerPos + " ! " + message);
        throw new SyntaxParserFatalErrorException(message, lexerPos);
    }

}
