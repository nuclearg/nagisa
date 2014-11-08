package com.github.nuclearg.nagisa.lang.ast;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.ast.stmt.Stmt;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 源文件
 * 
 * @author ng
 *
 */
public class CompilationUnit extends AstNode {
    /**
     * 程序中的各条语句
     */
    private final List<Stmt> stmts;

    CompilationUnit(SyntaxTreeNode node) {
        this.stmts = Stmt.resolveStmts(node.getChildren());
    }

    @Override
    public String toString() {
        return StringUtils.join(this.stmts, "");
    }
}
