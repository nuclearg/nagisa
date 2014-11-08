package com.github.nuclearg.nagisa.lang.ast.stmt;

import org.apache.commons.lang3.SystemUtils;

/**
 * 空语句
 * 
 * @author ng
 *
 */
public final class EmptyStmt extends Stmt {

    @Override
    protected String toString(String prefix) {
        return SystemUtils.LINE_SEPARATOR;
    }

}
