package com.github.nuclearg.nagisa.lang.parser;

import com.github.nuclearg.nagisa.lang.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;

/**
 * 表示空的语法规则
 * <p>
 * 在{@link SequenceRule}中会把此规则生成的{@link SyntaxTreeNode}给排除掉
 * </p>
 * 
 * @author ng
 *
 */
final class NullRule extends SyntaxRule {

    @Override
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        return new SyntaxTreeNode(this);
    }

    @Override
    boolean tryToken(LexTokenType tokenType) {
        return true;
    }

    @Override
    public String toString() {
        return "~empty~";
    }

}
