package com.github.nuclearg.nagisa.lang.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.github.nuclearg.nagisa.lang.ast.AstNode;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 语句
 * 
 * @author ng
 *
 */
public abstract class Stmt extends AstNode {
    public static List<Stmt> resolveStmts(List<SyntaxTreeNode> nodes) {
        List<Stmt> stmts = new ArrayList<>();

        nodes.forEach(n -> stmts.add(resolveStmt(n)));

        return stmts;
    }

    private static Stmt resolveStmt(SyntaxTreeNode node) {
        switch (node.ruleName) {
            case "VariableSetStmt":
                return new VariableSetStmt(node);
            case "IfStmt":
                return new IfStmt(node);
            case "ForStmt":
                return new ForStmt(node);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        return this.toString("");
    }

    protected abstract String toString(String prefix);

}
