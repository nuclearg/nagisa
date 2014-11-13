package com.github.nuclearg.nagisa.lang.util;

/**
 * 表示范围
 * 
 * @author ng
 *
 */
public final class Range {
    /**
     * 起始行号
     */
    private final int startRow;
    /**
     * 起始列号
     */
    private final int startColumn;
    /**
     * 结束行号
     */
    private final int endRow;
    /**
     * 结束列号
     */
    private final int endColumn;

    public Range(int startRow, int startColumn, int endRow, int endColumn) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }

    /** 起始行号 */
    public int getStartRow() {
        return this.startRow;
    }

    /** 起始列号 */
    public int getStartColumn() {
        return this.startColumn;
    }

    /** 结束行号 */
    public int getEndRow() {
        return this.endRow;
    }

    /** 结束列号 */
    public int getEndColumn() {
        return this.endColumn;
    }

    @Override
    public String toString() {
        return "[" + this.startRow + ":" + this.startColumn + "~" + this.endRow + ":" + this.endColumn + "]";
    }
}
