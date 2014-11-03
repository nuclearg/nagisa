package com.github.nuclearg.nagisa.lang.lex;

import java.util.Arrays;
import java.util.regex.Pattern;

import com.github.nuclearg.nagisa.lang.lexer.LexDefinition;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;

public class TestLexDefinition extends LexDefinition {

    public TestLexDefinition() {
        super(Arrays.asList(TestLexTokenType.values()));
    }

    private static enum TestLexTokenType implements LexTokenType {
        WORD("[0-9a-zA-Z]+", false),

        SPACE("( |\\t)+", true),

        LN("[\\r\\n]+", false),

        ;

        private final Pattern regex;
        private final boolean transparent;

        private TestLexTokenType(String regex, boolean transparent) {
            this.regex = Pattern.compile(regex);
            this.transparent = transparent;
        }

        @Override
        public Pattern regex() {
            return this.regex;
        }

        @Override
        public boolean transparent() {
            return this.transparent;
        }
    }

}
