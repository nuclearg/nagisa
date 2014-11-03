package com.github.nuclearg.nagisa.lang.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.nuclearg.nagisa.lang.util.Range;

/**
 * 词法解析器
 * 
 * @author ng
 *
 */
public class LexTokenizer {
    public static final LexTokenType ERROR = new LexTokenType() {

        @Override
        public boolean transparent() {
            return false;
        }

        @Override
        public Pattern regex() {
            return null;
        }

        @Override
        public String name() {
            return "ERROR_TOKEN";
        }
    };

    /**
     * 词法元素定义
     */
    private final LexDefinition definition;
    /**
     * 待解析的原始文本
     */
    private final String text;

    /**
     * 读取完前一个词之后的快照
     */
    private LexTokenizerSnapshot prevSnapshot;

    /**
     * 当前字符位置
     */
    private int pos;
    /**
     * 当前行号
     */
    private int row;
    /**
     * 当前列号
     */
    private int column;

    public LexTokenizer(LexDefinition definition, String text) {
        this.definition = definition;
        this.text = text;
    }

    /**
     * 判断是否已经读到了末尾
     */
    public boolean eof() {
        return this.pos >= this.text.length();
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
     * @return 下一个词法元素，如果无法与任何词法规则匹配则返回一个type为ERROR的token，如果遇到EOF则返回一个type为null的token
     */
    public LexToken next() {
        if (this.eof())
            return new LexToken(null, null, new Range(this.row, this.column, this.row, this.column));

        LexToken token = null;
        this.prevSnapshot = new LexTokenizerSnapshot(this, this.pos, this.row, this.column);

        // 遍历所有词法规则进行匹配
        for (LexTokenType type : this.definition.types) {
            Matcher m = type.regex().matcher(this.text);
            if (m.find(this.pos) && m.start() == this.pos) {
                // 正则匹配成功
                String str = this.text.substring(m.start(), m.end());
                this.pos += m.end() - m.start();

                // 处理行号和列号
                String copy = str.replaceAll("\\r\\n|\\r", "\n");
                int oldRow = this.row;
                int oldColumn = this.column;

                this.row += copy.chars().filter(ch -> ch == '\n').count();
                if (this.row != oldRow)
                    this.column = copy.length() - copy.lastIndexOf('\n');
                else
                    this.column += str.length();

                // 构造词法元素
                Range range = new Range(oldRow, oldColumn, this.row, this.column);
                token = new LexToken(type, str, range);
                break;
            }
        }

        // 如果无法与任何词法规则匹配则抛异常
        if (token == null)
            return new LexToken(ERROR, "" + text.charAt(this.pos), new Range(this.row, this.column, this.row, this.column));

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
        return new LexTokenizerSnapshot(this, this.pos, this.row, this.column);
    }

    /**
     * 获取读取当前词之前的快照
     */
    public LexTokenizerSnapshot prevSnapshot() {
        return this.prevSnapshot;
    }

    /**
     * 恢复指定的快照状态
     * 
     * @param snapshot
     *            要恢复的快照
     */
    private void restore(LexTokenizerSnapshot snapshot) {
        this.pos = snapshot.pos;
        this.row = snapshot.row;
        this.column = snapshot.column;
    }

    @Override
    public String toString() {
        return "[" + this.row + ":" + this.column + "] " + this.peek().toString();
    }
}
