package com.github.nuclearg.nagisa.frontend.ast;

import org.junit.Test;

import com.github.nuclearg.nagisa.frontend.ast.CompilationUnit;
import com.github.nuclearg.nagisa.frontend.ast.Context;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.identifier.IdentifierRegistry;
import com.github.nuclearg.nagisa.frontend.parser.NagisaSyntaxDefinition;

public class AstTest2 {
    @Test
    public void test() {
        SyntaxErrorReporter errorReporter = new SyntaxErrorReporter();
        IdentifierRegistry registry = new IdentifierRegistry(errorReporter);
        Context ctx = new Context(registry, errorReporter);

        String code = "dim a as integer\r\n";

        CompilationUnit cu = new CompilationUnit(NagisaSyntaxDefinition.parse(code), ctx);

        System.out.println(cu);
    }
}
