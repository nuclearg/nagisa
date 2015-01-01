package com.github.nuclearg.nagisa.frontend.ast;

import com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition.NagisaLexTokenType;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

class DefineFunctionPhase1Stmt extends DefineFunctionStmtBase {

    DefineFunctionPhase1Stmt(SyntaxTreeNode node, Context ctx) {
        super(node, ctx);

        boolean isFunction = this.functionKeywordType == NagisaLexTokenType.KEYWORD_FUNCTION || this.functionKeywordType == NagisaLexTokenType.KEYWORD_NATIVEFUNCTION;

        ctx.getRegistry().registerFunctionInfo(name, type, parameters, isFunction, node);
    }

    @Override
    protected String toString(String prefix) {
        return null;
    }

}
