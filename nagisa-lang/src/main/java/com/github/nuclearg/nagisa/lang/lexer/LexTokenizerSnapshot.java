package com.github.nuclearg.nagisa.lang.lexer;

/**
 * 词法解析器的快照
 * 
 * @author ng
 *
 */
public final class LexTokenizerSnapshot {
    /**
     * 词法解析器实例
     */
    private final LexTokenizer host;
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

    LexTokenizerSnapshot(LexTokenizer host, int pos, int row, int column) {
        this.host = host;
        this.pos = pos;
        this.row = row;
        this.column = column;
    }

    /** 词法解析器实例 */
    public LexTokenizer getHost() {
        return this.host;
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
