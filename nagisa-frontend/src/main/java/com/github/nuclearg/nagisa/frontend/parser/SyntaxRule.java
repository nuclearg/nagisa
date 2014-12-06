package com.github.nuclearg.nagisa.frontend.parser;

import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.lexer.LexToken;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition;
import com.github.nuclearg.nagisa.frontend.util.Position;

/**
 * 定义语法规则
 * 
 * @author ng
 *
 */
abstract class SyntaxRule {
    /**
     * 尝试进行语法分析
     * 
     * @param lexer
     *            词法分析器
     * @param errorReporter
     *            错误报告器
     * @return 解析出来的语法元素，或为null。如果为null，则保证将lexer的状态复原
     */
    abstract SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter);

    /**
     * 判断是否可以接受指定的{@link LexTokenType}作为这条规则的第一个词法元素
     * 
     * @param tokenType
     *            尝试进行匹配的{@link LexTokenType}
     * @param errorReporter
     *            错误报告器
     * @return
     */
    abstract boolean tryToken(LexTokenType tokenType, SyntaxErrorReporter errorReporter);

    /**
     * 尝试解析语法规则，如果当前词解析失败则尝试跳过当前的词，或者遇到EOL或EOF
     * 
     * @param lexer
     *            词法解析器
     * @param rule
     *            语法规则
     * @param errorReporter
     *            错误报告喝咖啡
     * @return 解析出来的语法树，或为null，为null时会将lexer恢复到原始状态
     */
    protected final SyntaxTreeNode tryParse(LexTokenizer lexer, SyntaxRule rule, SyntaxErrorReporter errorReporter) {
        Position position = lexer.position();

        SyntaxTreeNode node;

        while ((node = rule.parse(lexer, errorReporter)) == null) {
            // 跳过当前token
            LexToken token = lexer.next();

            // 如果当前token是EOF或EOL则报告解析失败
            if (token.getType() == null/* EOF */|| token.getType() == NagisaLexDefinition.NagisaLexTokenType.EOL /* EOL */) {
                // 恢复lexer，返回解析失败
                lexer.restore(position);
                return null;
            }
        }

        return node;
    }
}
