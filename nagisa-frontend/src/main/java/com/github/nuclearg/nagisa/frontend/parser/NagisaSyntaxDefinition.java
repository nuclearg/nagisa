package com.github.nuclearg.nagisa.frontend.parser;

import static com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition.NagisaLexTokenType.*;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.github.nuclearg.nagisa.frontend.error.Fatals;
import com.github.nuclearg.nagisa.frontend.error.NagisaFrontEndFatalErrorException;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenizer;
import com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition;

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
    public static SyntaxTreeNode parse(String text, SyntaxErrorReporter errorReporter) {
        try {
            LexTokenizer lexer = NagisaLexDefinition.lexer(text);

            SyntaxTreeNode node = INSTANCE.getRule(ROOT_RULE).parse(lexer, errorReporter);

            if (!lexer.eof())
                errorReporter.report(lexer.position(), Fatals.F1001, node);

            return node;
        } catch (NagisaFrontEndFatalErrorException ex) {
            return null;
        } catch (Exception ex) {
            errorReporter.report(Fatals.F0001, ExceptionUtils.getStackTrace(ex));
            return null;
        }
    }

    private NagisaSyntaxDefinition() {
        /*
         * 运算符优先级规则：
         * 
         * 括号 > 取负，取反 > 乘、除、求余 > 加、减 > 等于、不等于、大于、小于、大于等于、小于等于 > 与、或
         */

        define("AtomExprElement",
                or(
                        lex(LITERAL_INTEGER), // 整数字面量
                        lex(LITERAL_STRING), // 字符串字面量
                        seq(lex(SYMBOL_SUB), ref("AtomExprElement")), // 取负
                        seq(lex(SYMBOL_NOT), ref("AtomExprElement")), // 取反
                        seq(lex(SYMBOL_PARENTHESE_LEFT), ref("Expr"), lex(SYMBOL_PARENTHESE_RIGHT)), // 括号运算
                        seq(
                                lex(IDENTIFIER), // 普通标识符
                                opt(seq(lex(SYMBOL_PARENTHESE_LEFT), ref("ArgumentList"), lex(SYMBOL_PARENTHESE_RIGHT))))));// 函数调用
        define("MulDivModExprTerm",
                seq(
                        ref("AtomExprElement"),
                        opt(seq(
                                or(
                                        lex(SYMBOL_MUL),
                                        lex(SYMBOL_DIV),
                                        lex(SYMBOL_MOD)),
                                ref("MulDivModExprTerm")))));
        define("AddSubExprTerm",
                seq(
                        ref("MulDivModExprTerm"),
                        opt(seq(
                                or(
                                        lex(SYMBOL_ADD),
                                        lex(SYMBOL_SUB)),
                                ref("AddSubExprTerm")))));
        define("CompareExprTerm",
                seq(
                        ref("AddSubExprTerm"),
                        opt(seq(
                                or(
                                        lex(SYMBOL_EQ),
                                        lex(SYMBOL_NEQ),
                                        lex(SYMBOL_GT),
                                        lex(SYMBOL_GTE),
                                        lex(SYMBOL_LT),
                                        lex(SYMBOL_LTE)),
                                ref("CompareExprTerm")))));
        define("Expr",
                seq(
                        ref("CompareExprTerm"),
                        opt(seq(
                                or(
                                        lex(SYMBOL_AND),
                                        lex(SYMBOL_OR)),
                                ref("CompareExprTerm")))));

        // 空语句
        define("EmptyStmt",
                lex(EOL));

        // 定义变量
        define("DefineVariableStmt",
                seq(
                        lex(KEYWORD_DIM), lex(IDENTIFIER), lex(KEYWORD_AS), lex(IDENTIFIER), lex(EOL)));

        // 赋值语句
        define("VariableSetStmt",
                seq(
                        lex(KEYWORD_LET), lex(IDENTIFIER), lex(SYMBOL_LET), ref("Expr"), lex(EOL)));

        // if ... else ... end if
        define("IfStmt",
                seq(lex(KEYWORD_IF), ref("Expr"), lex(KEYWORD_THEN), lex(EOL),
                        ref("StmtList"),
                        or(
                                seq(lex(KEYWORD_ELSE), lex(EOL), ref("StmtList")),
                                nul()),
                        lex(KEYWORD_END), lex(KEYWORD_IF), lex(EOL)));

        // for ... next
        define("ForStmt",
                seq(
                        lex(KEYWORD_FOR), lex(IDENTIFIER), lex(SYMBOL_LET), ref("Expr"), lex(KEYWORD_TO), ref("Expr"), lex(EOL),
                        ref("StmtList"),
                        lex(KEYWORD_NEXT), lex(EOL)));

        // while ... end while
        define("WhileStmt",
                seq(lex(KEYWORD_WHILE), ref("Expr"), lex(EOL),
                        ref("StmtList"),
                        lex(KEYWORD_END), lex(KEYWORD_WHILE), lex(EOL)));

        // 调用方法
        define("CallSubStmt",
                seq(
                        lex(IDENTIFIER), ref("ArgumentList")));

        // 定义函数
        define("DefineFunctionStmt",
                seq(lex(KEYWORD_FUNCTION), lex(IDENTIFIER), lex(SYMBOL_PARENTHESE_LEFT), ref("ParamList"), lex(SYMBOL_PARENTHESE_RIGHT), lex(KEYWORD_AS), lex(IDENTIFIER), lex(EOL),
                        ref("StmtList"),
                        lex(KEYWORD_END), lex(KEYWORD_FUNCTION)));
        // 定义方法
        define("DefineSubStmt",
                seq(lex(KEYWORD_SUB), lex(IDENTIFIER), lex(SYMBOL_PARENTHESE_LEFT), ref("ParamList"), lex(SYMBOL_PARENTHESE_RIGHT), lex(EOL),
                        ref("StmtList"),
                        lex(KEYWORD_END), lex(KEYWORD_SUB)));
        // 返回语句
        define("ReturnStmt",
                seq(lex(KEYWORD_RETURN), opt(ref("Expr")), lex(EOL)));

        // 定义函数
        define("DefineNativeFunctionStmt",
                seq(lex(KEYWORD_NATIVEFUNCTION), lex(IDENTIFIER), lex(SYMBOL_PARENTHESE_LEFT), ref("ParamList"), lex(SYMBOL_PARENTHESE_RIGHT), lex(KEYWORD_AS), lex(IDENTIFIER), lex(EOL),
                        lex(LITERAL_STRING), lex(EOL),
                        lex(LITERAL_STRING), lex(EOL),
                        lex(KEYWORD_END), lex(KEYWORD_FUNCTION)));
        // 定义方法
        define("DefineNativeSubStmt",
                seq(lex(KEYWORD_NATIVESUB), lex(IDENTIFIER), lex(SYMBOL_PARENTHESE_LEFT), ref("ParamList"), lex(SYMBOL_PARENTHESE_RIGHT), lex(EOL),
                        lex(LITERAL_STRING), lex(EOL),
                        lex(LITERAL_STRING), lex(EOL),
                        lex(KEYWORD_END), lex(KEYWORD_SUB)));

        // 形参列表
        define("ParamList",
                seq(
                        opt(ref("Param")), rep(ref("RestParam"))));
        define("Param",
                seq(
                        lex(IDENTIFIER), lex(KEYWORD_AS), lex(IDENTIFIER)));
        define("RestParam",
                seq(
                        lex(SYMBOL_COMMA), ref("Param")));

        // 实参列表
        define("ArgumentList",
                seq(
                        opt(ref("Expr")), rep(ref("RestArgument"))));
        define("RestArgument",
                seq(
                        lex(SYMBOL_COMMA), ref("Expr")));

        // 普通语句
        define("Stmt",
                or(
                        ref("EmptyStmt"),
                        ref("DefineVariableStmt"),
                        ref("VariableSetStmt"),
                        ref("IfStmt"),
                        ref("ForStmt"),
                        ref("WhileStmt"),
                        ref("CallSubStmt"),
                        ref("ReturnStmt")));
        define("StmtList",
                rep(ref("Stmt")));

        // 编译单元
        define("CompilationUnit",
                seq(
                        ref("CompilaationUnitBodyStmts"),
                        ref("CompilationUnitDeclareStmts")));
        // 编译单元中的语句体部分
        define("CompilaationUnitBodyStmts",
                ref("StmtList"));
        // 编译单元中的定义部分
        define("CompilationUnitDeclareStmts",
                rep(or(
                        ref("DefineFunctionStmt"),
                        ref("DefineSubStmt"),
                        ref("DefineNativeFunctionStmt"),
                        ref("DefineNativeSubStmt"),
                        ref("EmptyStmt"))));
    }
}
