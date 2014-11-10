package com.github.nuclearg.nagisa.lang.parser;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerSnapshot;
import com.github.nuclearg.nagisa.lang.util.NagisaException;

public final class SyntaxParserFatalErrorException extends NagisaException {
    private static final long serialVersionUID = 1925680919646876547L;

    public SyntaxParserFatalErrorException(String message, LexTokenizerSnapshot snapshot) {
        super(snapshot + " " + message);
    }
}
