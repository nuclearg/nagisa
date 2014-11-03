package com.github.nuclearg.nagisa.lang.parser;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerSnapshot;
import com.github.nuclearg.nagisa.lang.parser.rule.SyntaxRule;

public class SyntaxErrorReporter {

    public void error(String message, LexTokenizer lexer, SyntaxRule rule) {
        LexTokenizerSnapshot snapshot = lexer.snapshot();
        System.err.println("[" + snapshot.row + "," + snapshot.column + "] " + message + " -- " + rule);
    }
}
