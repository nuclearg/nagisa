package com.github.nuclearg.nagisa.lang.lex;

import junit.framework.Assert;

import org.junit.Test;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerFactory;

public class LexTokenStreamReaderTest {

    @Test
    public void test() {
        String text = "AAA  BBB\r\n\r\n\r\r\n\n    CCC\n DDD \r\n EEE \r FFF";

        LexTokenizer r = LexTokenizerFactory.buildLexTokenizer(new TestLexDefinition(), text);

        Assert.assertEquals("AAA", r.next().getText());
        Assert.assertEquals("BBB", r.next().getText());
        r.next();
        Assert.assertEquals("CCC", r.next().getText());
        r.next();
        Assert.assertEquals("DDD", r.next().getText());
        r.next();
        Assert.assertEquals("EEE", r.next().getText());
        r.next();
        Assert.assertEquals("FFF", r.next().getText());
    }

    @Test
    public void test2() {

    }
}
