package com.github.nuclearg.nagisa.lang.ast;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.ast.stmt.Stmt;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

public class CompilationUnit extends AstNode {
    public final List<Stmt> stmts;

    public CompilationUnit(SyntaxTreeNode node) {
        this.stmts = Stmt.resolveStmts(node.children);
    }

    @Override
    public String toString() {
        return StringUtils.join(this.stmts, "");
    }
}
