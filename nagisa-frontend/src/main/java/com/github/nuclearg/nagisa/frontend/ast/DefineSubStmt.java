package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 方法定义
 * 
 * @author ng
 *
 */
public final class DefineSubStmt extends DefineFunctionStmtBase implements StmtBlockSupported {
    /**
     * 方法体
     */
    private final StmtBlock stmts;

    DefineSubStmt(SyntaxTreeNode node, Context ctx) {
        super(node, ctx);

        this.stmts = new StmtBlock(node.getChildren().get(6).getChildren(), ctx);
    }

    /** 方法体 */
    public StmtBlock getStmts() {
        return this.stmts;
    }

    @Override
    public void initStmtBlock() {
        this.stmts.init();
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "SUB " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ")" + SystemUtils.LINE_SEPARATOR
                + this.stmts.toString(prefix + "    ")
                + prefix + "END SUB" + SystemUtils.LINE_SEPARATOR;
    }
}
