package com.github.nuclearg.nagisa.lang.parser.rule;

import com.github.nuclearg.nagisa.lang.lexer.LexToken;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition;
import com.github.nuclearg.nagisa.lang.parser.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 对应一个词法单元的语法规则
 * 
 * @author ng
 *
 */
public final class LexRule implements SyntaxRule {
    /**
     * 词法元素类型
     */
    final LexTokenType tokenType;

    public LexRule(LexTokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        LexToken token = lexer.next();

        // 正常情况，词法分析器给出的词与期望的类型一致
        if (token.getType() == this.tokenType)
            return new SyntaxTreeNode(this, token);

        // 错误处理，遇到不匹配的词就直接忽略掉往后看
        while (token.getType() != this.tokenType) {
            errorReporter.error("遇到意外的符号 " + token + "，期望 " + this.tokenType, lexer.prevSnapshot());

            if (token.getType() == null)// EOF
                return null;
            if (token.getType() == NagisaLexDefinition.NagisaLexTokenType.EOL)// EOL
                return null;

            token = lexer.next();
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
