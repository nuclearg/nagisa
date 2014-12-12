package com.github.nuclearg.nagisa.frontend.parser;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.error.Fatals;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.lexer.LexToken;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition.NagisaLexTokenType;

/**
 * 表示或的语法关系，专用于解析语句。如果当前解析失败将会跳到下一行解析，直到解析出一条语句，或遇到EOF为止
 * 
 * @author ng
 *
 */
final class StmtRule extends SyntaxRule {
    private final List<SyntaxRule> rules;

    StmtRule(List<SyntaxRule> rules) {
        this.rules = Collections.unmodifiableList(rules);
    }

    @Override
    boolean tryToken(LexTokenType tokenType, SyntaxErrorReporter errorReporter) {
        for (SyntaxRule rule : this.rules)
            if (rule.tryToken(tokenType, errorReporter))
                return true;
        return false;
    }

    @Override
    SyntaxTreeNode parse(LexTokenizer lexer, SyntaxErrorReporter errorReporter) {
        while (true) {
            // 判断当前是什么语句
            SyntaxRule stmtRule;
            while (true) {
                LexToken token = lexer.peek();
                if (token.getType() == null) // eof
                    errorReporter.report(lexer.position(), Fatals.F1002);

                stmtRule = this.findStmtRule(token, errorReporter);
                if (stmtRule != null)
                    break;
                else
                    lexer.next();
            }

            // 解析这条语句，如果报错则直接跳到下一行
            SyntaxTreeNode stmtNode = stmtRule.parse(lexer, errorReporter);
            if (stmtNode != null)
                return stmtNode;
            else
                // 跳到下一行
                while (lexer.next() != null && lexer.next().getType() != NagisaLexTokenType.EOL)
                    ;
        }
    }

    @Override
    public String toString() {
        return StringUtils.join(this.rules.stream().map(r -> "(" + r + ")").toArray(), "|");
    }

    private SyntaxRule findStmtRule(LexToken token, SyntaxErrorReporter errorReporter) {
        for (SyntaxRule rule : this.rules)
            if (rule.tryToken(token.getType(), errorReporter))
                return rule;
        return null;
    }
}
