package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.error.Fatals;
import com.github.nuclearg.nagisa.frontend.lexer.LexToken;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition.NagisaLexTokenType;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;
import com.github.nuclearg.nagisa.frontend.symbol.FunctionSymbol;
import com.github.nuclearg.nagisa.frontend.symbol.TypeSymbol;
import com.github.nuclearg.nagisa.frontend.symbol.VariableSymbol;

/**
 * 表达式
 * 
 * @author ng
 *
 */
public final class Expr {
    /**
     * 表达式类型
     */
    private final TypeSymbol type;
    /**
     * 表达式运算符
     */
    private final ExprOperator operator;
    /**
     * 表达式的字面量，可能为null
     */
    private final String text;
    /**
     * 该表达式的各个子表达式，可能为null
     */
    private final List<Expr> children;

    /**
     * 表达式的优先级是否被重新定义过（两边是否被括上了括号）
     */
    private final boolean priorityRedefined;

    private Expr(TypeSymbol type, ExprOperator operator, String text) {
        this.type = type;
        this.operator = operator;
        this.text = text;
        this.children = Collections.emptyList();
        this.priorityRedefined = false;
    }

    private Expr(TypeSymbol type, ExprOperator operator, String text, Expr child) {
        this.type = type;
        this.operator = operator;
        this.text = text;
        this.children = Arrays.asList(child);
        this.priorityRedefined = false;
    }

    private Expr(TypeSymbol type, ExprOperator operator, String text, Expr left, Expr right) {
        this.type = type;
        this.operator = operator;
        this.text = text;
        this.children = Arrays.asList(left, right);
        this.priorityRedefined = false;
    }

    public Expr(TypeSymbol exprType, ExprOperator operator, String text, List<Expr> children) {
        this.type = exprType;
        this.operator = operator;
        this.text = text;
        this.children = Collections.unmodifiableList(children);
        this.priorityRedefined = false;
    }

    private Expr(Expr expr, boolean priorityRedefined) {
        this.type = expr.type;
        this.operator = expr.operator;
        this.text = expr.text;
        this.children = expr.children;
        this.priorityRedefined = priorityRedefined;
    }

    /** 表达式类型 */
    public TypeSymbol getType() {
        return this.type;
    }

    /** 表达式运算符 */
    public ExprOperator getOperator() {
        return this.operator;
    }

    /** 表达式的字面量，可能为null */
    public String getText() {
        return this.text;
    }

    /** 该表达式的各个子表达式 */
    public Iterable<Expr> getChildren() {
        return this.children;
    }

    @Override
    public String toString() {
        String text;

        if (this.operator == ExprOperator.FunctionInvocation)
            text = this.text + "(" + StringUtils.join(this.children, ", ") + ")";
        else
            switch (this.children.size()) {
                case 0:
                    text = this.text;
                    break;
                case 1:
                    text = this.text + this.children.get(0);
                    break;
                case 2:
                    text = this.children.get(0) + " " + this.text + " " + this.children.get(1);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

        if (this.priorityRedefined)
            return "(" + text + ")";
        else
            return text;
    }

    /**
     * 根据语法节点构建出一个表达式
     * 
     * @param node
     *            表示表达式的语法节点
     * @param ctx
     *            上下文
     * @return 表达式语法树
     */
    static Expr buildExpr(SyntaxTreeNode node, Context ctx) {
        LexToken firstToken = null;
        if (node.getChildren().size() > 1)
            firstToken = node.getChildren().get(0).getToken();

        // 判断表达式有几个组成部分
        switch (node.getChildren().size()) {
            case 0:
                return buildNoneParamExpr(node, ctx);
            case 1:
                return buildExpr(node.getChildren().get(0), ctx);
            case 2:
                return buildSingleParamExpr(node, ctx);
            case 3:
                // 判断是双目运算符还是括号表达式
                if (firstToken != null && firstToken.getType() == NagisaLexTokenType.SYMBOL_PARENTHESE_LEFT)
                    return buildParentheseExpr(node, ctx);
                else
                    return buildDoubleParamExpr(node, ctx);
            case 4:
                return buildFunctionCallExpr(node, ctx);
            default:
                ctx.getErrorReporter().report(node, Fatals.F0001, "unsupported expr children size. size: " + node.getChildren().size() + ", node: " + node);
                return null;
        }
    }

    private static Expr buildNoneParamExpr(SyntaxTreeNode node, Context ctx) {
        LexToken token = node.getToken();
        String text = token.getText();

        switch ((NagisaLexTokenType) token.getType()) {
            case LITERAL_INTEGER:
                return new Expr(TypeSymbol.INTEGER, ExprOperator.IntegerLiteral, text);
            case LITERAL_STRING:
                return new Expr(TypeSymbol.STRING, ExprOperator.StringLiteral, text);
            case IDENTIFIER:
                VariableSymbol info = ctx.getRegistry().lookupVariableSymbol(text, node);
                if (info == null)
                    return null;

                return new Expr(info.getType(), ExprOperator.VariableRef, text);
            default:
                ctx.getErrorReporter().report(node, Fatals.F0001, "unsupported param0 expr. token: " + token);
                return null;
        }
    }

    private static Expr buildSingleParamExpr(SyntaxTreeNode node, Context ctx) {
        LexToken opToken = node.getChildren().get(0).getToken();
        Expr param = buildExpr(node.getChildren().get(1), ctx);

        if (param == null)
            return null;

        switch ((NagisaLexTokenType) opToken.getType()) {
            case SYMBOL_SUB:
                if (param.type != TypeSymbol.INTEGER)
                    ctx.getErrorReporter().report(node, Errors.E1101, TypeSymbol.INTEGER, param.type);
                return new Expr(TypeSymbol.INTEGER, ExprOperator.IntegerNegative, opToken.getText(), param);
            case SYMBOL_NOT:
                if (param.type != TypeSymbol.STRING)
                    ctx.getErrorReporter().report(node, Errors.E1101, TypeSymbol.BOOLEAN, param.type);
                return new Expr(TypeSymbol.BOOLEAN, ExprOperator.BooleanNot, opToken.getText(), param);
            default:
                ctx.getErrorReporter().report(node, Fatals.F0001, "unsupported param1 expr. token: " + opToken);
                return null;
        }
    }

    private static Expr buildDoubleParamExpr(SyntaxTreeNode node, Context ctx) {
        Expr left = buildExpr(node.getChildren().get(0), ctx);
        LexToken opToken = node.getChildren().get(1).getToken();
        Expr right = buildExpr(node.getChildren().get(2), ctx);

        if (left == null || right == null)
            return null;

        // 找到与当前的运算符、左表达式类型、右表达式类型都匹配的运算符
        for (OperatorInfo info : OPERATORS)
            if (info.opTokenType == opToken.getType() && info.leftType == left.type && info.rightType == right.type)
                return new Expr(info.resultType, info.operator, opToken.getText(), left, right);

        // 找不到，报错
        ctx.getErrorReporter().report(node, Errors.E1102, left.type, right.type, opToken.getText());
        return null;
    }

    private static Expr buildParentheseExpr(SyntaxTreeNode node, Context ctx) {
        Expr expr = buildExpr(node.getChildren().get(1), ctx);
        if (expr == null)
            return null;

        // 如果expr是字面量则把括号去掉（因为毫无意义）
        if (expr.children.isEmpty())
            return expr;

        return new Expr(expr, true);
    }

    private static Expr buildFunctionCallExpr(SyntaxTreeNode node, Context ctx) {
        // 函数名
        String name = node.getChildren().get(0).getToken().getText();
        // 实参列表
        List<SyntaxTreeNode> argNodes = node.getChildren().get(2).getChildren();
        List<Expr> args = argNodes.stream()
                .map(n -> "RestArgument".equals(n.getRuleName()) ? n.getChildren().get(1) : n)
                .map(n -> buildExpr(n, ctx))
                .collect(Collectors.toList());

        // 实参类型列表
        List<TypeSymbol> argTypes = args.stream()
                .map(o -> o.getType())
                .collect(Collectors.toList());

        FunctionSymbol function = ctx.getRegistry().lookupFunctionSymbol(name, argTypes, true, node);
        if (function == null)
            // 随便拼一个什么东西返回去
            return new Expr(TypeSymbol.VOID, ExprOperator.FunctionInvocation, name, args);

        return new Expr(function.getType(), ExprOperator.FunctionInvocation, function.getName(), args);
    }

    private static class OperatorInfo {
        private final LexTokenType opTokenType;
        private final TypeSymbol leftType;
        private final TypeSymbol rightType;

        private final ExprOperator operator;
        private final TypeSymbol resultType;

        OperatorInfo(LexTokenType opTokenType, TypeSymbol leftType, TypeSymbol rightType, ExprOperator operator, TypeSymbol resultType) {
            this.opTokenType = opTokenType;
            this.leftType = leftType;
            this.rightType = rightType;
            this.operator = operator;
            this.resultType = resultType;
        }

    }

    private static final List<OperatorInfo> OPERATORS = Arrays.asList(
            new OperatorInfo(NagisaLexTokenType.SYMBOL_ADD, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerAdd, TypeSymbol.INTEGER),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_SUB, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerSub, TypeSymbol.INTEGER),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_MUL, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerMul, TypeSymbol.INTEGER),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_DIV, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerDiv, TypeSymbol.INTEGER),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_MOD, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerMod, TypeSymbol.INTEGER),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_ADD, TypeSymbol.STRING, TypeSymbol.STRING, ExprOperator.StringAdd, TypeSymbol.STRING),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_EQ, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerEq, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_NEQ, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerNeq, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_GT, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerGt, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_GTE, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerGte, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_LT, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerLt, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_LTE, TypeSymbol.INTEGER, TypeSymbol.INTEGER, ExprOperator.IntegerLte, TypeSymbol.BOOLEAN),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_EQ, TypeSymbol.STRING, TypeSymbol.STRING, ExprOperator.StringEq, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_NEQ, TypeSymbol.STRING, TypeSymbol.STRING, ExprOperator.StringNeq, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_GT, TypeSymbol.STRING, TypeSymbol.STRING, ExprOperator.StringGt, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_GTE, TypeSymbol.STRING, TypeSymbol.STRING, ExprOperator.StringGte, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_LT, TypeSymbol.STRING, TypeSymbol.STRING, ExprOperator.StringLt, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_LTE, TypeSymbol.STRING, TypeSymbol.STRING, ExprOperator.StringLte, TypeSymbol.BOOLEAN),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_EQ, TypeSymbol.BOOLEAN, TypeSymbol.BOOLEAN, ExprOperator.BooleanEq, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_NEQ, TypeSymbol.BOOLEAN, TypeSymbol.BOOLEAN, ExprOperator.BooleanNeq, TypeSymbol.BOOLEAN),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_AND, TypeSymbol.BOOLEAN, TypeSymbol.BOOLEAN, ExprOperator.BooleanAnd, TypeSymbol.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_OR, TypeSymbol.BOOLEAN, TypeSymbol.BOOLEAN, ExprOperator.BooleanOr, TypeSymbol.BOOLEAN)
            );
}