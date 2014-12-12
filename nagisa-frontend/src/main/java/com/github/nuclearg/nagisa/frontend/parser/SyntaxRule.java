package com.github.nuclearg.nagisa.frontend.parser;

import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenizer;

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
}
