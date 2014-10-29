package com.github.nuclearg.nagisa.compiler.lex;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;

import com.github.nuclearg.nagisa.util.Range;

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

    /**
     * 输出的词法元素列表
     */
    private final List<LexToken> historyTokens = new ArrayList<>();
    /**
     * 回滚的词法元素列表
     */
    private final Stack<LexToken> rollbackTokens = new Stack<>();

    public LexTokenizer(LexDefinition definition, String text) {
        this.definition = definition;
        this.text = text;
    }

    /**
     * 获取下一个词法元素
     * 
     * @return 下一个词法元素，或为null
     */
    public LexToken next() {
        if (!this.rollbackTokens.isEmpty()) {
            LexToken token = this.rollbackTokens.pop();
            this.historyTokens.add(token);
            return token;
        }

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

        if (token == null)
            return null;

        if (token.type.transparent())
            return this.next();

        this.historyTokens.add(token);
        return token;
    }

    /**
     * 回退一个词法元素
     */
    public void rollback() {
        LexToken token = this.historyTokens.remove(this.historyTokens.size() - 1);
        this.rollbackTokens.push(token);
    }
}
