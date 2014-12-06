package com.github.nuclearg.nagisa.frontend.ast;

import org.junit.Assert;
import org.junit.Test;

public class CodeTest {
    @Test
    public void test() {
        String code = "// let a = 3  - - - - - 4\r\n";

        ParseResult result = ParserUtils.parse(code);

        Assert.assertTrue(result.errors.isEmpty());

        System.out.println(result.cu);
    }
}
