package com.github.nuclearg.nagisa.lang.parser.rule;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.parser.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 定义语法规则
 * 
 * @author ng
 *
 */
public interface SyntaxRule {
    /**
     * 尝试进行语法分析
     * 
     * @param lexer
     *            词法分析器
     * @param errorReporter
     *            错误报告器
     * @return 解析出来的语法元素
     */
    public SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter);

    /**
     * 判断是否可以接受指定的{@link LexTokenType}作为这条规则的第一个词法元素
     */
    public boolean tryToken(LexTokenType tokenType);
}
