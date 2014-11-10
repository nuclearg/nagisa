package com.github.nuclearg.nagisa.lang.parser;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerSnapshot;

final class SyntaxErrorReporter {
    private int count;

    void error(String message, LexTokenizerSnapshot lexerPos) {
        System.err.println(lexerPos + " " + message);

        if (++count >= 20)
            fatal("错误过多，中断解析过程", lexerPos);
    }

    void fatal(String message, LexTokenizerSnapshot lexerPos) {
        System.err.println(lexerPos + " ! " + message);
        throw new SyntaxParserFatalErrorException(message, lexerPos);
    }

}
