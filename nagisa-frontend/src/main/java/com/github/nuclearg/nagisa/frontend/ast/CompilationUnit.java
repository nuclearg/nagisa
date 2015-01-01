package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;

import java.util.List;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 源文件
 * 
 * @author ng
 *
 */
public final class CompilationUnit {
    /**
     * 程序体中的各条语句
     */
    private final List<Stmt> mainStmts;

    /**
     * 函娄定义列表
     */
    private final List<Stmt> definitionStmts;

    CompilationUnit(SyntaxTreeNode node, Context ctx) {
        this.mainStmts = Stmt.buildStmts(node.getChildren().get(0).getChildren(), ctx);
        this.definitionStmts = Stmt.buildStmts(node.getChildren().get(1).getChildren(), ctx);
    }

    /** 程序体中的各条语句 */
    public Iterable<Stmt> getMainStmts() {
        return this.mainStmts;
    }

    /** 函数和方法声明语句 */
    public Iterable<Stmt> getDefinitionStmts() {
        return this.definitionStmts;
    }

    @Override
    public String toString() {
        return Stmt.toString(this.mainStmts, "") + LN
                + Stmt.toString(this.definitionStmts, "");
    }
}
