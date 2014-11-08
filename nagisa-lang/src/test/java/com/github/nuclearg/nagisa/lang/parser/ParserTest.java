package com.github.nuclearg.nagisa.lang.parser;

import org.junit.Assert;
import org.junit.Test;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerFactory;
import com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition;

public class ParserTest {
    @Test
    public void testExpr() {
        test("Expr", "1");
        test("Expr", "-1");
        test("Expr", "1 + 2 * 3 / 4");
        test("Expr", "(1) + ((2) * (3 / 4))");
        test("Expr", "A + B");
        test("Expr", "-------------1");
    }

    @Test
    public void testLet() {
        test("VariableSetStmt", "let A=1\n");
    }

    @Test
    public void testIf() {
        test("IfStmt", "if a>1 then\n let a=1\nendif");
        test("IfStmt", "if a>1 then \n if b>1 then \nlet a=a+b\n endif\n endif\n");
    }

    @Test
    public void testFor() {
        test("ForStmt", "for i=1 to 5\nLET a=2\nnext\n");
    }

    @Test
    public void testCompilationiUnit() {
    }

    private void test(String ruleName, String code) {
        LexTokenizer lexer = LexTokenizerFactory.buildLexTokenizer(new NagisaLexDefinition(), code);

        NagisaSyntaxDefinition definition = new NagisaSyntaxDefinition();

        System.out.println(ruleName + ": " + code);
        SyntaxTreeNode node = definition.getRule(ruleName).parse(lexer, new SyntaxErrorReporter());
        System.out.println(" ==> " + node);

        Assert.assertTrue(lexer.eof());

    }
}
