package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.SystemUtils;

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
        return SystemUtils.LINE_SEPARATOR;
    }

}
