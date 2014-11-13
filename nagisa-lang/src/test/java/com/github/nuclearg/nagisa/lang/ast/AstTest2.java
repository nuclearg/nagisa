package com.github.nuclearg.nagisa.lang.ast;

import org.junit.Test;

import com.github.nuclearg.nagisa.lang.parser.NagisaSyntaxDefinition;

public class AstTest2 {
    @Test
    public void test() {
        String code = "let a = rnd()\r\n";

        CompilationUnit cu = new CompilationUnit(NagisaSyntaxDefinition.parse(code));

        System.out.println(cu);
    }
}
