package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 函数定义
 * 
 * @author ng
 *
 */
public final class DefineFunctionStmt extends DefineFunctionStmtBase implements StmtBlockSupported {
    /**
     * 函数体
     */
    private final StmtBlock stmts;

    DefineFunctionStmt(SyntaxTreeNode node, Context ctx) {
        super(node, ctx);

        this.stmts = new StmtBlock(node.getChildren().get(6).getChildren(), ctx);
    }

    /** 函数体 */
    public StmtBlock getStmts() {
        return this.stmts;
    }

    @Override
    public void initStmtBlock() {
        this.stmts.init();
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "FUNCTION " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ") AS " + this.type + SystemUtils.LINE_SEPARATOR
                + this.stmts.toString(prefix + "    ")
                + prefix + "END FUNCTION" + SystemUtils.LINE_SEPARATOR;
    }

}
