package com.github.nuclearg.nagisa.frontend.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.util.Position;
import com.github.nuclearg.nagisa.frontend.util.Range;

/**
 * 词法解析器
 * 
 * @author ng
 *
 */
public final class LexTokenizer {
    public static final LexTokenType ERROR = new LexTokenType() {

        @Override
        public boolean transparent() {
            return false;
        }

        @Override
        public String literal() {
            return null;
        }

        @Override
        public Pattern regex() {
            return null;
        }

        @Override
        public String name() {
            return "ERROR_TOKEN";
        }

        @Override
        public String toString() {
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
    private Position prevPosition;

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

    LexTokenizer(LexDefinition definition, String text) {
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
     * @return 下一个词法元素，如果无法与任何词法规则匹配则返回一个type为ERROR的token，如果遇到EOF则返回一个type为null的token
     */
    public LexToken peek() {
        Position snapshot = this.position();
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
            return new LexToken(null, null, new Range(this.position(), this.position()));

        LexToken token = null;
        this.prevPosition = new Position(this.pos, this.row, this.column);

        // 遍历所有词法规则进行匹配
        for (LexTokenType type : this.definition.getTypes()) {
            String str = null;

            if (type.literal() != null) {
                // 使用字面量进行匹配
                if (this.text.toUpperCase().indexOf(type.literal().toUpperCase(), this.pos) == this.pos) {
                    str = type.literal();

                    // 检查之后的字符不是[a-zA-Z0-9]，避免将“LET asdf = 0”中标识符“asdf”中的as解析为KEYWORD_AS

                    // 只有当前token的最后一个字符是[a-zA-Z0-9]时才需要进行这种检查，否则“let a=0”中的等于号会校验不过去
                    String lastChar = str.charAt(type.literal().length() - 1) + "";
                    if (StringUtils.isAlphanumeric(lastChar)) {
                        int posAfterToken = this.pos + str.length();

                        // 没到EOF
                        if (this.text.length() != posAfterToken) {
                            // 取下一个字符
                            String nextChar = this.text.charAt(posAfterToken) + "";

                            if (StringUtils.isAlphanumeric(nextChar))
                                str = null;
                        }
                    }
                }
            } else {
                // 使用正则进行匹配
                Matcher m = type.regex().matcher(this.text);
                if (m.find(this.pos) && m.start() == this.pos)
                    str = this.text.substring(m.start(), m.end());
            }

            if (str != null) {
                this.pos += str.length();

                // 处理行号和列号
                String copy = str.replaceAll("\\r\\n|\\r", "\n");
                int oldRow = this.row;

                this.row += copy.chars().filter(ch -> ch == '\n').count();
                if (this.row != oldRow)
                    this.column = copy.length() - copy.lastIndexOf('\n');
                else
                    this.column += str.length();

                // 构造词法元素
                Range range = new Range(this.prevPosition, this.position());
                token = new LexToken(type, str, range);
                break;
            }
        }

        // 如果无法与任何词法规则匹配则跳过一个字符，并返回ERROR
        if (token == null) {
            char ch = text.charAt(this.pos);
            this.pos++;
            this.column++;

            Range range = new Range(this.prevPosition, this.position());
            return new LexToken(ERROR, "" + ch, range);
        }

        // 如果这个词是透明的，则向调用者返回下一个词
        if (token.getType().transparent())
            return this.next();

        return token;
    }

    /**
     * 创建当前的快照
     * 
     * @return 当前的快照
     */
    public Position position() {
        return new Position(this.pos, this.row, this.column);
    }

    /**
     * 获取读取当前词之前的快照
     */
    public Position prevPosition() {
        return this.prevPosition;
    }

    /**
     * 恢复指定的快照状态
     * 
     * @param position
     *            要恢复的快照
     */
    public void restore(Position position) {
        this.pos = position.getPos();
        this.row = position.getRow();
        this.column = position.getColumn();
    }

    @Override
    public String toString() {
        return "[" + this.row + ":" + this.column + "] " + this.peek().toString();
    }
}
