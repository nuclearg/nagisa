package com.github.nuclearg.nagisa.lang.lexer;

/**
 * 词法解析器的快照
 * 
 * @author ng
 *
 */
public class LexTokenizerSnapshot {
    /**
     * 词法解析器实例
     */
    public final LexTokenizer host;
    /**
     * 当前位置
     */
    public final int pos;
    /**
     * 当前行号
     */
    public final int row;
    /**
     * 当前列号
     */
    public final int column;

    LexTokenizerSnapshot(LexTokenizer host, int pos, int row, int column) {
        this.host = host;
        this.pos = pos;
        this.row = row;
        this.column = column;
    }

}
