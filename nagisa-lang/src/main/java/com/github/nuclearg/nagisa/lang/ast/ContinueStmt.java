package com.github.nuclearg.nagisa.lang.ast;

import org.apache.commons.lang3.SystemUtils;

/**
 * continue语句
 * 
 * @author ng
 *
 */
public final class ContinueStmt extends Stmt {

    @Override
    public String toString(String prefix) {
        return prefix + "CONTINUE" + SystemUtils.LINE_SEPARATOR;
    }
}
