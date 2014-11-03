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
        define("ExprElement",
                or(
                        lex(INTEGER), // 整数字面量
                        lex(STRING), // 字符串字面量
                        lex(SYMBOL), // 变量调用
                        seq(lex(OPERATOR_PARENTHESE_LEFT), ref("Expr"), lex(OPERATOR_PARENTHESE_RIGHT))));
        define("Expr", or(
                seq(lex(OPERATOR_SUB), ref("Expr")), // 取负数
                seq(lex(OPERATOR_NOT), ref("Expr")), // 取逻辑反
                seq(ref("ExprElement"), or(
                        // 算术运算
                        seq(or(lex(OPERATOR_ADD), lex(OPERATOR_SUB), lex(OPERATOR_MUL), lex(OPERATOR_DIV)), ref("Expr")),
                        // 比较运算
                        seq(or(lex(OPERATOR_EQ), lex(OPERATOR_NEQ), lex(OPERATOR_GT), lex(OPERATOR_GTE), lex(OPERATOR_LT), lex(OPERATOR_LTE)), ref("Expr")),
                        // 逻辑运算
                        seq(or(lex(OPERATOR_AND), lex(OPERATOR_OR)), ref("Expr")),
                        nul()))));

        define("VariableSetStmt", // 赋值语句
                seq(lex(KEYWORD_LET), lex(SYMBOL), lex(OPERATOR_LET), ref("Expr"), lex(EOL)));

        define("IfStmt", // if...else...endif语句
                seq(
                        lex(KEYWORD_IF), ref("Expr"), lex(KEYWORD_THEN), seq(lex(EOL), rep(ref("Stmt"))),
                        or(
                                seq(lex(KEYWORD_ELSE), rep(ref("Stmt"))),
                                nul()),
                        lex(KEYWORD_ENDIF), lex(EOL)));

        define("ForStmt", // for...next语句
                seq(lex(KEYWORD_FOR), lex(SYMBOL), lex(OPERATOR_LET), ref("Expr"), lex(KEYWORD_TO), ref("Expr"), lex(EOL), rep(ref("Stmt")), lex(KEYWORD_NEXT), lex(EOL)));

        define("WhileStmt", // while...wend语句
                seq(lex(KEYWORD_WHILE), ref("Expr"), lex(EOL), rep(ref("Stmt")), lex(KEYWORD_WEND), lex(EOL)));

        define("Stmt", or( // 一条语句
                ref("VariableSetStmt"),
                ref("IfStmt"),
                ref("ForStmt")));

        define("CompilationUnit", rep(ref("Stmt")));
    }
}
