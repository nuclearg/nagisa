package com.github.nuclearg.nagisa.lang.parser;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;

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
     * @return 解析出来的语法元素
     */
    abstract SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter);

    /**
     * 判断是否可以接受指定的{@link LexTokenType}作为这条规则的第一个词法元素
     */
    abstract boolean tryToken(LexTokenType tokenType);
}
