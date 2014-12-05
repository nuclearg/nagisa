package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.SystemUtils;

/**
 * break语句
 * 
 * @author ng
 *
 */
public final class BreakStmt extends Stmt {
    @Override
    public String toString(String prefix) {
        return prefix + "BREAK" + SystemUtils.LINE_SEPARATOR;
    }
}
