package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 源文件
 * 
 * @author ng
 *
 */
public final class CompilationUnit implements StmtBlockSupported {
    /**
     * 程序体中的各条语句
     */
    private final StmtBlock bodyStmts;

    /**
     * 函数和方法声明语句
     */
    private final StmtBlock functionStmts;

    CompilationUnit(SyntaxTreeNode node, Context ctx) {
        this.bodyStmts = new StmtBlock(node.getChildren().get(0).getChildren(), ctx);
        this.functionStmts = new StmtBlock(node.getChildren().get(1).getChildren(), ctx);

        // 最后初始化
        this.initStmtBlock();
    }

    /** 程序体中的各条语句 */
    public Iterable<Stmt> getBodyStmts() {
        return this.bodyStmts;
    }

    /** 函数和方法声明语句 */
    public Iterable<Stmt> getFunctionStmts() {
        return this.functionStmts;
    }

    @Override
    public void initStmtBlock() {
        // 必须先初始化函数声明再初始化程序体，否则程序体中调用函数时会找不到符号

        this.functionStmts.init();

        this.bodyStmts.init();
    }

    @Override
    public String toString() {
        return this.bodyStmts + SystemUtils.LINE_SEPARATOR
                + this.functionStmts;
    }

}
