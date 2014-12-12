package com.github.nuclearg.nagisa.frontend.ast;

import org.junit.Assert;
import org.junit.Test;

import com.github.nuclearg.nagisa.frontend.ast.NagisaFrontend.LoadResult;

public class CodeTest {
    @Test
    public void test() {
        String code = "function a()\r\nend function\r\n";

        LoadResult result = NagisaFrontend.loadCompilationUnit(code);

        Assert.assertTrue(result.getErrors().isEmpty());

        System.out.println(result.getCu());
    }
}
