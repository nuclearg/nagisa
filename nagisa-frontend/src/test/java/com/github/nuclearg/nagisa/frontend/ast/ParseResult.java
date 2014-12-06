package com.github.nuclearg.nagisa.frontend.ast;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.ast.CompilationUnit;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorLogItem;

class ParseResult {
    final String code;
    final CompilationUnit cu;
    final List<SyntaxErrorLogItem> errors;

    ParseResult(String code, CompilationUnit cu, List<SyntaxErrorLogItem> errors) {
        this.code = code;
        this.cu = cu;
        this.errors = errors;
    }

}
