package com.github.nuclearg.nagisa.lang.ast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition;
import com.github.nuclearg.nagisa.lang.parser.NagisaSyntaxDefinition;
import com.github.nuclearg.nagisa.lang.parser.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.util.InputStreamUtils;

public class AstTest {
    @Test
    public void test1() throws IOException {
        test("test1.txt");
    }

    @Test
    public void testLet() throws IOException {
        test("let.txt");
    }

    @Test
    public void testIf() throws IOException {
        test("if.txt");
    }

    @Test
    public void testFor() throws IOException {
        test("for.txt");
    }

    @Test
    public void testWhile() throws IOException {
        test("while.txt");
    }

    private void test(String filename) throws IOException {
        System.out.println("AST test: " + filename);

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("ast/" + filename);

        String code = InputStreamUtils.read(is, Charset.forName("utf-8"));

        LexTokenizer lexer = new LexTokenizer(new NagisaLexDefinition(), code);

        NagisaSyntaxDefinition definition = new NagisaSyntaxDefinition();

        CompilationUnit cu = new CompilationUnit(definition.rules.get("CompilationUnit").parse(lexer, new SyntaxErrorReporter()));

        System.out.println(cu);

        Assert.assertTrue(lexer.eof());
    }
}
