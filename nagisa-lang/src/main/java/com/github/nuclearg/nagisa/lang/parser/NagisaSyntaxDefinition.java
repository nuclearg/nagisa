package com.github.nuclearg.nagisa.lang.parser;

import static com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition.NagisaLexTokenType.*;

/**
 * nagisa的语法定义
 * 
 * @author ng
 *
 */
public class NagisaSyntaxDefinition extends SyntaxDefinition {

    public NagisaSyntaxDefinition() {
        // 数学运算表达式
        define("NumberExprElement",
                or(
                        lex(INTEGER),
                        lex(SYMBOL),
                        seq(lex(OPERATOR_PARENTHESE_LEFT), ref("Expr"), lex(OPERATOR_PARENTHESE_RIGHT))));
        define("NumberMulDivModExprTerm",
                seq(
                        ref("NumberExprElement"),
                        or(
                                seq(
                                        or(
                                                lex(OPERATOR_MUL),
                                                lex(OPERATOR_DIV),
                                                lex(OPERATOR_MOD)),
                                        ref("NumberMulDivModExprTerm")),
                                nul())));
        define("NumberExpr",
                or(
                        seq(lex(OPERATOR_SUB), ref("NumberExpr")),
                        seq(
                                ref("NumberMulDivModExprTerm"),
                                or(
                                        seq(
                                                or(
                                                        lex(OPERATOR_ADD),
                                                        lex(OPERATOR_SUB)),
                                                ref("NumberExpr")),
                                        nul()))));

        // 字符串运算表达式
        define("StringExprElement",
                or(
                        lex(STRING),
                        lex(STRING_SYMBOL),
                        seq(lex(OPERATOR_PARENTHESE_LEFT), ref("StringExpr"), lex(OPERATOR_PARENTHESE_RIGHT))));
        define("StringExpr",
                seq(
                        ref("StringExprElement"),
                        or(
                                seq(lex(OPERATOR_ADD), ref("StringExpr")),
                                nul())));

        // 逻辑运算表达式
        define("BooleanExprElement",
                or(
                        seq(
                                ref("NumberExpr"),
                                or(
                                        lex(OPERATOR_EQ),
                                        lex(OPERATOR_NEQ),
                                        lex(OPERATOR_GT),
                                        lex(OPERATOR_GTE),
                                        lex(OPERATOR_LT),
                                        lex(OPERATOR_LTE)),
                                ref("NumberExpr")),
                        seq(
                                ref("StringExpr"),
                                or(
                                        lex(OPERATOR_EQ),
                                        lex(OPERATOR_NEQ),
                                        lex(OPERATOR_GT),
                                        lex(OPERATOR_GTE),
                                        lex(OPERATOR_LT),
                                        lex(OPERATOR_LTE)),
                                ref("StringExpr")),
                        seq(lex(OPERATOR_PARENTHESE_LEFT), ref("BooleanExpr"), lex(OPERATOR_PARENTHESE_RIGHT))));
        define("BooleanExpr",
                or(
                        seq(lex(OPERATOR_NOT), ref("BooleanExpr")),
                        seq(
                                ref("BooleanExprElement"),
                                or(
                                        seq(
                                                or(
                                                        lex(OPERATOR_AND),
                                                        lex(OPERATOR_OR)),
                                                ref("BooleanExpr")),
                                        nul()))));

        // 空语句
        define("EmptyStmt",
                lex(EOL));

        // 赋值语句
        define("VariableSetStmt",
                seq(
                        lex(KEYWORD_LET),
                        or(
                                seq(lex(SYMBOL), lex(OPERATOR_LET), ref("NumberExpr"), lex(EOL)),
                                seq(lex(STRING_SYMBOL), lex(OPERATOR_LET), ref("StringExpr"), lex(EOL)))));

        // if...else...endif
        define("IfStmt",
                seq(lex(KEYWORD_IF), ref("BooleanExpr"), lex(KEYWORD_THEN), lex(EOL), rep(ref("Stmt")), or(seq(lex(KEYWORD_ELSE), lex(EOL), rep(ref("Stmt"))), nul()), lex(KEYWORD_ENDIF), lex(EOL)));

        // for...next
        define("ForStmt",
                seq(lex(KEYWORD_FOR), lex(SYMBOL), lex(OPERATOR_LET), ref("NumberExpr"), lex(KEYWORD_TO), ref("NumberExpr"), lex(EOL), rep(ref("Stmt")), lex(KEYWORD_NEXT), lex(EOL)));

        // while...wend
        define("WhileStmt",
                seq(lex(KEYWORD_WHILE), ref("BooleanExpr"), lex(EOL), rep(ref("Stmt")), lex(KEYWORD_WEND), lex(EOL)));

        // 一条语句
        define("Stmt",
                or(
                        ref("EmptyStmt"),
                        ref("VariableSetStmt"),
                        ref("IfStmt"),
                        ref("ForStmt"),
                        ref("WhileStmt")));

        define("CompilationUnit", rep(ref("Stmt")));
    }
}
