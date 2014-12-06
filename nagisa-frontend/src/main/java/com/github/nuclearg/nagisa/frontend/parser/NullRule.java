package com.github.nuclearg.nagisa.frontend.parser;

import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenizer;

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
    boolean tryToken(LexTokenType tokenType, SyntaxErrorReporter errorReporter) {
        return true;
    }

    @Override
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        return new SyntaxTreeNode(this);
    }

    @Override
    public String toString() {
        return "~empty~";
    }

}
