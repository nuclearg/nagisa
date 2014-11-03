package com.github.nuclearg.nagisa.lang.lex;

import junit.framework.Assert;

import org.junit.Test;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;

public class LexTokenStreamReaderTest {

    @Test
    public void test() {
        String text = "AAA  BBB\r\n\r\n\r\r\n\n    CCC\n DDD \r\n EEE \r FFF";

        LexTokenizer r = new LexTokenizer(new TestLexDefinition(), text);

        Assert.assertEquals("AAA", r.next().text);
        Assert.assertEquals("BBB", r.next().text);
        r.next();
        Assert.assertEquals("CCC", r.next().text);
        r.next();
        Assert.assertEquals("DDD", r.next().text);
        r.next();
        Assert.assertEquals("EEE", r.next().text);
        r.next();
        Assert.assertEquals("FFF", r.next().text);
    }

    @Test
    public void test2() {
        

    }
}
