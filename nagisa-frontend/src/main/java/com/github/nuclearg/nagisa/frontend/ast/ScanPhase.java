package com.github.nuclearg.nagisa.frontend.ast;

/**
 * 解析的遍数
 * 
 * @author ng
 *
 */
enum ScanPhase {
    /**
     * 解析各种声明
     */
    ScanDeclaration,
    /**
     * 解析每条语句，实际构建语法树
     */
    ScanStmt,
}
