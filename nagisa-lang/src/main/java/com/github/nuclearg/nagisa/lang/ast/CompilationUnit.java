package com.github.nuclearg.nagisa.lang.ast;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

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

    CompilationUnit(SyntaxTreeNode node) {
        this.stmts = Stmt.resolveStmts(node.getChildren());
    }

    /**
     * 获取程序中的各条语句
     */
    public Iterable<Stmt> getStmts() {
        return this.stmts;
    }

    @Override
    public String toString() {
        return StringUtils.join(this.stmts, "");
    }
}
