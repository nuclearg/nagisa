package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 空语句
 * 
 * @author ng
 *
 */
public final class EmptyStmt extends Stmt {

    EmptyStmt(SyntaxTreeNode node, Context ctx) {
    }

    @Override
    protected String toString(String prefix) {
        return LN;
    }

}
