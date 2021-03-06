package com.github.nuclearg.nagisa.lang.ast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import com.github.nuclearg.nagisa.lang.parser.NagisaSyntaxDefinition;
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

        CompilationUnit cu = new CompilationUnit(NagisaSyntaxDefinition.parse(code));

        System.out.println(cu);
    }
}
