package com.github.nuclearg.nagisa.lang.parser.rule;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenType;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.LexTokenizerSnapshot;
import com.github.nuclearg.nagisa.lang.parser.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;
import com.github.nuclearg.nagisa.lang.util.Range;

/**
 * 表示空的语法规则
 * 
 * @author ng
 *
 */
public class NullRule implements SyntaxRule {

    @Override
    public SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        LexTokenizerSnapshot snapshot = lexer.snapshot();

        Range range = new Range(snapshot.row, snapshot.column, snapshot.row, snapshot.column);

        return new SyntaxTreeNode(this, range);
    }

    @Override
    public boolean tryToken(LexTokenType tokenType) {
        return true;
    }

    @Override
    public String toString() {
        return "~empty~";
    }

}
