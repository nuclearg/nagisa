package com.github.nuclearg.nagisa.lang.ast;

import java.util.List;
import java.util.stream.Collectors;

import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

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
     * @param ctx
     *            上下文
     * @return 语句列表
     */
    static List<Stmt> resolveStmts(List<SyntaxTreeNode> nodes, Context ctx) {
        return nodes.stream().map(n -> resolveStmt(n, ctx)).collect(Collectors.toList());
    }

    /**
     * 将一个语法节点解析为单条语句
     * 
     * @param node
     *            语法节点
     * @param ctx
     *            上下文
     * @return 语句
     */
    private static Stmt resolveStmt(SyntaxTreeNode node, Context ctx) {
        switch (node.getRuleName()) {
            case "EmptyStmt":
                return new EmptyStmt();
            case "VariableSetStmt":
                return new VariableSetStmt(node, ctx);
            case "IfStmt":
                return new IfStmt(node, ctx);
            case "ForStmt":
                return new ForStmt(node, ctx);
            case "WhileStmt":
                return new WhileStmt(node, ctx);
            case "BreakStmt":
                return new BreakStmt();
            case "ContinueStmt":
                return new ContinueStmt();
            case "CallSubStmt":
                return new CallSubStmt(node, ctx);
            case "DefineVariableStmt":
                return new DefineVariableStmt(node, ctx);
            case "DefineFunctionStmt":
            case "DefineSubStmt":
                return new DefineFunctionStmt(node, ctx);
            case "DefineNativeFunctionStmt":
            case "DefineNativeSubStmt":
                return new DefineNativeFunctionStmt(node, ctx);
            case "ReturnStmt":
                return new ReturnStmt(node, ctx);
            default:
                throw new UnsupportedOperationException(node.getRuleName() + ", node: " + node);
        }
    }
}
