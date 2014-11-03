package com.github.nuclearg.nagisa.lang.ast;

import org.junit.Assert;
import org.junit.Test;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition;
import com.github.nuclearg.nagisa.lang.parser.NagisaSyntaxDefinition;
import com.github.nuclearg.nagisa.lang.parser.SyntaxErrorReporter;

public class AstTest {
    @Test
    public void test() {
        test("let a=1\nlet b=2\nfor i=0 to 5\n let a=3\nlet b=4\nlet c=a+b\nif i>3 then \nlet d=i\n if c>3 then\n if d>5 then  \n let a=a+b\nlet b=a+b\n endif\n endif\n endif\n next\n");
    }

    private void test(String code) {
        LexTokenizer lexer = new LexTokenizer(new NagisaLexDefinition(), code);

        NagisaSyntaxDefinition definition = new NagisaSyntaxDefinition();

        CompilationUnit cu = new CompilationUnit(definition.rules.get("CompilationUnit").parse(lexer, new SyntaxErrorReporter()));

        System.out.println(cu);

        Assert.assertTrue(lexer.eof());

    }
}
