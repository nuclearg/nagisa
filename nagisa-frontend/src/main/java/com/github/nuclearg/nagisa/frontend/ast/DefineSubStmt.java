package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;
import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.TAB;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 方法定义
 * 
 * @author ng
 *
 */
public final class DefineSubStmt extends DefineFunctionStmtBase {
    /**
     * 方法体
     */
    private final List<Stmt> stmts;

    DefineSubStmt(SyntaxTreeNode node, Context ctx) {
        super(node, ctx);

        this.stmts = Stmt.buildStmts(node.getChildren().get(6).getChildren(), ctx);

        ctx.popLevel();
    }

    /** 方法体 */
    public Iterable<Stmt> getStmts() {
        return this.stmts;
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "SUB " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ")" + LN
                + Stmt.toString(this.stmts, prefix + TAB)
                + prefix + "END SUB" + LN;
    }
}
