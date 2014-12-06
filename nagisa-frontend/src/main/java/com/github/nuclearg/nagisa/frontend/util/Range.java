package com.github.nuclearg.nagisa.frontend.util;

/**
 * 表示范围
 * 
 * @author ng
 *
 */
public final class Range {
    public static final Range EMPTY = new Range(Position.EMPTY, Position.EMPTY);

    /**
     * 起始位置
     */
    private final Position startPosition;
    /**
     * 结束位置
     */
    private final Position endPosition;

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

    public Range(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.startRow = startPosition.getRow();
        this.startColumn = startPosition.getColumn();
        this.endRow = endPosition.getRow();
        this.endColumn = endPosition.getColumn();
    }

    /** 起始位置 */
    public Position getStartPosition() {
        return this.startPosition;
    }

    /** 结束位置 */
    public Position getEndPosition() {
        return this.endPosition;
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
