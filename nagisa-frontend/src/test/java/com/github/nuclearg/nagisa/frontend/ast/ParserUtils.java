package com.github.nuclearg.nagisa.frontend.ast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.identifier.IdentifierRegistry;
import com.github.nuclearg.nagisa.frontend.parser.NagisaSyntaxDefinition;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

class ParserUtils {
    static ParseResult parse(String code) {
        SyntaxErrorReporter errorReporter = new SyntaxErrorReporter();
        IdentifierRegistry registry = new IdentifierRegistry(errorReporter);
        Context ctx = new Context(registry, errorReporter);

        SyntaxTreeNode node = NagisaSyntaxDefinition.parse(code, errorReporter);
        if (node == null)
            return new ParseResult(code, null, errorReporter.getErrors());

        CompilationUnit cu = new CompilationUnit(node, ctx);

        return new ParseResult(code, cu, errorReporter.getErrors());
    }

    static ParseResult parse(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        String code = InputStreamUtils.read(is, Charset.forName("utf-8")) + SystemUtils.LINE_SEPARATOR;

        System.out.println("===== " + file.getName() + " =====");
        System.out.println(code);

        return parse(code);
    }
}
