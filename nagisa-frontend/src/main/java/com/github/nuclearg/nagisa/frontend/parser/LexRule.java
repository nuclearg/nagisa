package com.github.nuclearg.nagisa.frontend.parser;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.lexer.LexToken;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenizer;

/**
 * 对应一个词法单元的语法规则
 * 
 * @author ng
 *
 */
final class LexRule extends SyntaxRule {
    /**
     * 词法元素类型
     */
    final LexTokenType tokenType;

    LexRule(LexTokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        LexToken token = lexer.peek();

        // 如果词法分析器给出的词与期望的类型不一致则报错返回
        if (token.getType() != this.tokenType) {
            // 清理一下待输出的内容
            String current = token.getText();
            current = StringUtils.replace(current, "\r", "<CR>");
            current = StringUtils.replace(current, "\n", "<LF>");

            String exptected = this.tokenType.literal() != null ? this.tokenType.literal() : this.tokenType.toString();

            errorReporter.report(Errors.E0001, lexer.prevPosition(), current, exptected);
            return null;
        }

        // 解析成功，返回语法树节点
        lexer.next();
        return new SyntaxTreeNode(this, token);
    }

    @Override
    boolean tryToken(LexTokenType tokenType) {
        return this.tokenType == tokenType;
    }

    @Override
    public String toString() {
        return this.tokenType.toString();
    }

}
