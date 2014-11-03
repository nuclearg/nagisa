package com.github.nuclearg.nagisa.lang.lexer;

import java.util.regex.Matcher;

import com.github.nuclearg.nagisa.lang.util.NagisaException;
import com.github.nuclearg.nagisa.lang.util.Range;

/**
 * 词法解析器
 * 
 * @author ng
 *
 */
public class LexTokenizer {
    /**
     * 词法元素定义
     */
    private final LexDefinition definition;
    /**
     * 待解析的原始文本
     */
    private final String text;

    /**
     * 当前字符位置
     */
    private int currentPos;
    /**
     * 当前行号
     */
    private int currentRow;
    /**
     * 当前列号
     */
    private int currentColumn;

    public LexTokenizer(LexDefinition definition, String text) {
        this.definition = definition;
        this.text = text;
    }

    /**
     * 判断是否已经读到了末尾
     */
    public boolean eof() {
        return this.currentPos >= this.text.length();
    }

    /**
     * 向前看一个词法元素，但不从流中读出
     * 
     * @return 同{@link #next()}
     */
    public LexToken peek() {
        LexTokenizerSnapshot snapshot = this.snapshot();
        LexToken token = this.next();
        this.restore(snapshot);
        return token;
    }

    /**
     * 获取下一个词法元素
     * 
     * @return 下一个词法元素，如果无法与任何词法规则匹配则抛异常，如果遇到EOF则返回一个type为null的token
     */
    public LexToken next() {
        if (this.eof())
            return new LexToken(null, null, new Range(this.currentRow, this.currentColumn, this.currentRow, this.currentColumn));

        LexToken token = null;

        // 遍历所有词法规则进行匹配
        for (LexTokenType type : this.definition.types) {
            Matcher m = type.regex().matcher(this.text);
            if (m.find(this.currentPos) && m.start() == this.currentPos) {
                // 正则匹配成功
                String str = this.text.substring(m.start(), m.end());
                this.currentPos += m.end() - m.start();

                // 处理行号和列号
                String copy = str.replaceAll("\\r\\n|\\r", "\n");
                int oldRow = this.currentRow;
                int oldColumn = this.currentColumn;

                this.currentRow += copy.chars().filter(ch -> ch == '\n').count();
                if (this.currentRow != oldRow)
                    this.currentColumn += copy.length() - copy.lastIndexOf('\n');
                else
                    this.currentColumn += str.length();

                // 构造词法元素
                Range range = new Range(oldRow, oldColumn, this.currentRow, this.currentColumn);
                token = new LexToken(type, str, range);
                break;
            }
        }

        // 如果无法与任何词法规则匹配则抛异常
        if (token == null)
            throw new NagisaException("词法分析失败。遇到无法解析的字符。row: " + this.currentRow + ", column: " + this.currentColumn);

        if (token.type.transparent())
            return this.next();

        return token;
    }

    /**
     * 创建当前的快照
     * 
     * @return 当前的快照
     */
    public LexTokenizerSnapshot snapshot() {
        return new LexTokenizerSnapshot(this, this.currentPos, this.currentRow, this.currentColumn);
    }

    /**
     * 恢复指定的快照状态
     * 
     * @param snapshot
     *            要恢复的快照
     */
    public void restore(LexTokenizerSnapshot snapshot) {
        if (snapshot.host != this)
            throw new NagisaException("snapshot not from this tokenizer");

        this.currentPos = snapshot.pos;
        this.currentRow = snapshot.row;
        this.currentColumn = snapshot.column;
    }
}
