package com.github.nuclearg.nagisa.lang.ast;

import java.util.List;

import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;
import com.github.nuclearg.nagisa.lang.util.ListUtils;

/**
 * 语句
 * 
 * @author ng
 *
 */
public abstract class Stmt {

    @Override
    public String toString() {
        return this.toString("");
    }

    /**
     * 需要实现类实现的带前缀（缩进）的toString
     * 
     * @param prefix
     *            前缀
     * @return 带前缀的字符串形式
     */
    protected abstract String toString(String prefix);

    /**
     * 将一系列语法节点解析为语句列表
     * 
     * @param nodes
     *            语法节点列表
     * @return 语句列表
     */
    static List<Stmt> resolveStmts(List<SyntaxTreeNode> nodes) {
        return ListUtils.transform(nodes, n -> resolveStmt(n));
    }

    /**
     * 将一个语法节点解析为单条语句
     * 
     * @param node
     *            语法节点
     * @return 语句
     */
    private static Stmt resolveStmt(SyntaxTreeNode node) {
        switch (node.getRuleName()) {
            case "EmptyStmt":
                return new EmptyStmt();
            case "VariableSetStmt":
                return new VariableSetStmt(node);
            case "IfStmt":
                return new IfStmt(node);
            case "ForStmt":
                return new ForStmt(node);
            case "WhileStmt":
                return new WhileStmt(node);
            case "BreakStmt":
                return new BreakStmt();
            case "ContinueStmt":
                return new ContinueStmt();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
