package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;
import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.TAB;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 函数定义
 * 
 * @author ng
 *
 */
public final class DefineFunctionStmt extends DefineFunctionStmtBase {
    /**
     * 函数体
     */
    private final List<Stmt> stmts;

    DefineFunctionStmt(SyntaxTreeNode node, Context ctx) {
        super(node, ctx);

        this.stmts = Stmt.buildStmts(node.getChildren().get(8).getChildren(), ctx);

        ctx.popLevel();
    }

    /** 函数体 */
    public Iterable<Stmt> getStmts() {
        return this.stmts;
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "FUNCTION " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ") AS " + this.type + LN
                + Stmt.toString(this.stmts, prefix + TAB)
                + prefix + "END FUNCTION" + LN;
    }

}
