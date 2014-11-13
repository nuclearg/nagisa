package com.github.nuclearg.nagisa.lang.util;

/**
 * 表示范围
 * 
 * @author ng
 *
 */
public class Range {
    /**
     * 起始行号
     */
    public final int startRow;
    /**
     * 起始列号
     */
    public final int startColumn;
    /**
     * 结束行号
     */
    public final int endRow;
    /**
     * 结束列号
     */
    public final int endColumn;

    public Range(int startRow, int startColumn, int endRow, int endColumn) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }

    @Override
    public String toString() {
        return "[" + this.startRow + ":" + this.startColumn + "~" + this.endRow + ":" + this.endColumn + "]";
    }
}
