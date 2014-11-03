package com.github.nuclearg.nagisa.lang.parser.rule;

import com.github.nuclearg.nagisa.lang.lexer.LexToken;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerSnapshot;
import com.github.nuclearg.nagisa.lang.parser.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 对应一个词法单元的语法规则
 * 
 * @author ng
 *
 */
public class LexRule implements SyntaxRule {
    /**
     * 词法元素类型
     */
    final LexTokenType tokenType;

    public LexRule(LexTokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        LexTokenizerSnapshot snapshot = lexer.snapshot();

        LexToken token = lexer.next();

        if (token.type != this.tokenType) {
            errorReporter.error("token类型不匹配", lexer, this);
            lexer.restore(snapshot);
            return null;
        }

        return new SyntaxTreeNode(this, token);
    }

    @Override
    public boolean tryToken(LexTokenType tokenType) {
        return this.tokenType == tokenType;
    }

    @Override
    public String toString() {
        return this.tokenType.toString();
    }

}
