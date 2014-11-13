package com.github.nuclearg.nagisa.lang.parser;

import static com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition.NagisaLexTokenType.*;

import com.github.nuclearg.nagisa.lang.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition;

/**
 * nagisa的语法定义
 * 
 * @author ng
 *
 */
public final class NagisaSyntaxDefinition extends SyntaxDefinition {
    private static final NagisaSyntaxDefinition INSTANCE = new NagisaSyntaxDefinition();
    private static final String ROOT_RULE = "CompilationUnit";

    /**
     * 对文本行语法解析
     * 
     * @param text
     *            待解析的文本
     * @return 解析出的语法节点树
     */
    public static SyntaxTreeNode parse(String text) {
        LexTokenizer lexer = NagisaLexDefinition.lexer(text);
        SyntaxErrorReporter errorReporter = new SyntaxErrorReporter();

        SyntaxTreeNode node = INSTANCE.getRule(ROOT_RULE).parse(lexer, errorReporter);

        if (!lexer.eof())
            errorReporter.fatal("有剩余字符无法被解析", lexer.snapshot());

        return node;

    }

    private NagisaSyntaxDefinition() {
        // 数学运算表达式
        define("IntegerExprElement",
                or(
                        lex(LITERAL_INTEGER),
                        seq(
                                lex(IDENTIFIER_INTEGER),
                                or(
                                        seq(lex(SYMBOL_PARENTHESE_LEFT), ref("ArgumentList"), lex(SYMBOL_PARENTHESE_RIGHT)),
                                        nul())),
                        seq(lex(SYMBOL_PARENTHESE_LEFT), ref("IntegerExpr"), lex(SYMBOL_PARENTHESE_RIGHT))));
        define("IntegerMulDivModExprTerm",
                seq(
                        ref("IntegerExprElement"),
                        or(
                                seq(
                                        or(
                                                lex(SYMBOL_MUL),
                                                lex(SYMBOL_DIV),
                                                lex(SYMBOL_MOD)),
                                        ref("IntegerMulDivModExprTerm")),
                                nul())));
        define("IntegerExpr",
                or(
                        seq(lex(SYMBOL_SUB), ref("IntegerExpr")),
                        seq(
                                ref("IntegerMulDivModExprTerm"),
                                or(
                                        seq(
                                                or(
                                                        lex(SYMBOL_ADD),
                                                        lex(SYMBOL_SUB)),
                                                ref("IntegerExpr")),
                                        nul()))));

        // 字符串运算表达式
        define("StringExprElement",
                or(
                        lex(LITERAL_STRING),
                        seq(
                                lex(IDENTIFIER_STRING),
                                or(
                                        seq(lex(SYMBOL_PARENTHESE_LEFT), ref("ArgumentList"), lex(SYMBOL_PARENTHESE_RIGHT)),
                                        nul())),
                        seq(lex(SYMBOL_PARENTHESE_LEFT), ref("StringExpr"), lex(SYMBOL_PARENTHESE_RIGHT))));
        define("StringExpr",
                seq(
                        ref("StringExprElement"),
                        or(
                                seq(lex(SYMBOL_ADD), ref("StringExpr")),
                                nul())));

        // 逻辑运算表达式
        define("BooleanExprElement",
                or(
                        seq(
                                ref("IntegerExpr"),
                                or(
                                        lex(SYMBOL_EQ),
                                        lex(SYMBOL_NEQ),
                                        lex(SYMBOL_GT),
                                        lex(SYMBOL_GTE),
                                        lex(SYMBOL_LT),
                                        lex(SYMBOL_LTE)),
                                ref("IntegerExpr")),
                        seq(
                                ref("StringExpr"),
                                or(
                                        lex(SYMBOL_EQ),
                                        lex(SYMBOL_NEQ),
                                        lex(SYMBOL_GT),
                                        lex(SYMBOL_GTE),
                                        lex(SYMBOL_LT),
                                        lex(SYMBOL_LTE)),
                                ref("StringExpr")),
                        seq(lex(SYMBOL_PARENTHESE_LEFT), ref("BooleanExpr"), lex(SYMBOL_PARENTHESE_RIGHT))));
        define("BooleanExpr",
                or(
                        seq(lex(SYMBOL_NOT), ref("BooleanExpr")),
                        seq(
                                ref("BooleanExprElement"),
                                or(
                                        seq(
                                                or(
                                                        lex(SYMBOL_AND),
                                                        lex(SYMBOL_OR)),
                                                ref("BooleanExpr")),
                                        nul()))));

        // 形参列表
        define("ParamList",
                seq(
                        or(
                                lex(IDENTIFIER_INTEGER),
                                lex(IDENTIFIER_STRING),
                                nul()),
                        rep(ref("RestParam"))));
        define("RestParam",
                seq(
                        lex(SYMBOL_COMMA),
                        or(
                                lex(IDENTIFIER_INTEGER),
                                lex(IDENTIFIER_STRING))));

        // 实参列表
        define("ArgumentList",
                seq(
                        or(
                                ref("IntegerExpr"),
                                ref("StringExpr"),
                                nul()),
                        rep(ref("RestArgument"))));
        define("RestArgument",
                seq(
                        lex(SYMBOL_COMMA),
                        or(
                                ref("IntegerExpr"),
                                ref("StringExpr"))));

        // 空语句
        define("EmptyStmt",
                lex(EOL));

        // 赋值语句
        define("VariableSetStmt",
                seq(
                        lex(KEYWORD_LET),
                        or(
                                seq(lex(IDENTIFIER_INTEGER), lex(SYMBOL_LET), ref("IntegerExpr"), lex(EOL)),
                                seq(lex(IDENTIFIER_STRING), lex(SYMBOL_LET), ref("StringExpr"), lex(EOL)))));

        // if ... else ... end if
        define("IfStmt",
                seq(lex(KEYWORD_IF), ref("BooleanExpr"), lex(KEYWORD_THEN), lex(EOL), ref("IfBodyStmtList"), or(seq(lex(KEYWORD_ELSE), lex(EOL), ref("IfBodyStmtList")), nul()), lex(KEYWORD_END), lex(KEYWORD_IF), lex(EOL)));
        define("IfBodyStmtList",
                rep(ref("Stmt")));

        // for ... next
        define("ForStmt",
                seq(
                        lex(KEYWORD_FOR), lex(IDENTIFIER_INTEGER), lex(SYMBOL_LET), ref("IntegerExpr"), lex(KEYWORD_TO), ref("IntegerExpr"), lex(EOL),
                        ref("ForBodyStmtList"),
                        lex(KEYWORD_NEXT), lex(EOL)));
        define("ForBodyStmtList",
                rep(
                or(
                        ref("Stmt"),
                        ref("BreakStmt"),
                        ref("ContinueStmt"))));

        // while ... end while
        define("WhileStmt",
                seq(lex(KEYWORD_WHILE), ref("BooleanExpr"), lex(EOL),
                        ref("ForBodyStmtList"),
                        lex(KEYWORD_END), lex(KEYWORD_WHILE), lex(EOL)));

        // break
        define("BreakStmt",
                seq(lex(KEYWORD_BREAK), lex(EOL)));

        // continue
        define("ContinueStmt",
                seq(lex(KEYWORD_CONEINUE), lex(EOL)));

        // 调用方法
        define("CallSubStmt",
                seq(
                        lex(IDENTIFIER_INTEGER),
                        ref("ArgumentList")));

        // 定义函数
        define("DefineFunctionStmt",
                seq(lex(KEYWORD_FUNCTION), or(lex(IDENTIFIER_INTEGER), lex(IDENTIFIER_STRING)), lex(SYMBOL_PARENTHESE_LEFT), ref("ParamList"), lex(SYMBOL_PARENTHESE_RIGHT), lex(EOL),
                        rep(
                        or(ref("Stmt"),
                                ref("ReturnFromFunctionStmt"))),
                        lex(KEYWORD_END), lex(KEYWORD_FUNCTION)
                ));

        // 从函数中返回（带参数）
        define("ReturnFromFunctionStmt",
                seq(lex(KEYWORD_RETURN), or(ref("IntegerExpr"), ref("StringExpr")), lex(EOL)));

        // 定义方法
        define("DefineSubStmt",
                seq(lex(KEYWORD_SUB), lex(IDENTIFIER_INTEGER), lex(SYMBOL_PARENTHESE_LEFT), ref("ParamList"), lex(SYMBOL_PARENTHESE_RIGHT), lex(EOL),
                        rep(
                        or(ref("Stmt"),
                                ref("ReturnFromSubStmt"))),
                        lex(KEYWORD_END), lex(KEYWORD_SUB)
                ));

        // 不带参数的return
        define("ReturnFromSubStmt",
                seq(lex(KEYWORD_RETURN), lex(EOL)));

        // 普通语句
        define("Stmt",
                or(
                        ref("EmptyStmt"),
                        ref("VariableSetStmt"),
                        ref("IfStmt"),
                        ref("ForStmt"),
                        ref("WhileStmt"),
                        ref("CallSubStmt")));

        // 编译单元
        define("CompilationUnit",
                seq(
                        rep(ref("Stmt")),
                        rep(
                        or(
                                ref("DefineFunctionStmt"),
                                ref("DefineSubStmt")))));
    }
}
