package com.github.nuclearg.nagisa.frontend.ast;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 源文件
 * 
 * @author ng
 *
 */
public class CompilationUnit {
    /**
     * 程序中的各条语句
     */
    private final List<Stmt> stmts;

    CompilationUnit(SyntaxTreeNode node, Context ctx) {
        this.stmts = Stmt.resolveStmts(node.getChildren(), ctx);
    }

    /**
     * 获取程序中的各条语句
     */
    public Iterable<Stmt> getStmts() {
        return this.stmts;
    }

    @Override
    public String toString() {
        return Stmt.toString(this.stmts, "");
    }
}
