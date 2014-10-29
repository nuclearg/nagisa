package com.github.nuclearg.nagisa.util;

public class Range {
    public final int startRow;
    public final int startColumn;
    public final int endRow;
    public final int endColumn;

    public Range(int startRow, int startColumn, int endRow, int endColumn) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }

}
