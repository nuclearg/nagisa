package com.github.nuclearg.nagisa.frontend.util;

/**
 * 词法解析器的快照
 * 
 * @author ng
 *
 */
public final class Position {
    public static final Position EMPTY = new Position(0, 0, 0);

    /**
     * 当前位置
     */
    private final int pos;
    /**
     * 当前行号
     */
    private final int row;
    /**
     * 当前列号
     */
    private final int column;

    public Position(int pos, int row, int column) {
        this.pos = pos;
        this.row = row;
        this.column = column;
    }

    /** 当前位置 */
    public int getPos() {
        return this.pos;
    }

    /** 当前行号 */
    public int getRow() {
        return this.row;
    }

    /** 当前列号 */
    public int getColumn() {
        return this.column;
    }

    @Override
    public String toString() {
        return "[" + (this.row + 1) + ":" + (this.column + 1) + "]";
    }
}
