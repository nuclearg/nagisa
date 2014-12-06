package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.error.Fatals;
import com.github.nuclearg.nagisa.frontend.identifier.FunctionIdentifierInfo;
import com.github.nuclearg.nagisa.frontend.identifier.IdentifierType;
import com.github.nuclearg.nagisa.frontend.identifier.VariableIdentifierInfo;
import com.github.nuclearg.nagisa.frontend.lexer.LexToken;
import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition.NagisaLexTokenType;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

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
    private final IdentifierType type;
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

    private Expr(IdentifierType type, ExprOperator operator, String text) {
        this.type = type;
        this.operator = operator;
        this.text = text;
        this.children = Collections.emptyList();
        this.priorityRedefined = false;
    }

    private Expr(IdentifierType type, ExprOperator operator, String text, Expr child) {
        this.type = type;
        this.operator = operator;
        this.text = text;
        this.children = Arrays.asList(child);
        this.priorityRedefined = false;
    }

    private Expr(IdentifierType type, ExprOperator operator, String text, Expr left, Expr right) {
        this.type = type;
        this.operator = operator;
        this.text = text;
        this.children = Arrays.asList(left, right);
        this.priorityRedefined = false;
    }

    public Expr(IdentifierType exprType, ExprOperator operator, String text, List<Expr> children) {
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
    public IdentifierType getType() {
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
    static Expr resolveExpr(SyntaxTreeNode node, Context ctx) {
        LexToken firstToken = null;
        if (node.getChildren().size() > 1)
            firstToken = node.getChildren().get(0).getToken();

        // 判断表达式有几个组成部分
        switch (node.getChildren().size()) {
            case 0:
                return resolveNoneParamExpr(node, ctx);
            case 1:
                return resolveExpr(node.getChildren().get(0), ctx);
            case 2:
                return resolveSingleParamExpr(node, ctx);
            case 3:
                // 判断是双目运算符还是括号表达式
                if (firstToken != null && firstToken.getType() == NagisaLexTokenType.SYMBOL_PARENTHESE_LEFT)
                    return resolveParentheseExpr(node, ctx);
                else
                    return resolveDoubleParamExpr(node, ctx);
            case 4:
                return resolveFunctionCallExpr(node, ctx);
            default:
                ctx.errorReporter.report(node, Fatals.F0001, "unsupported expr children size. size: " + node.getChildren().size() + ", node: " + node);
                return null;
        }
    }

    private static Expr resolveNoneParamExpr(SyntaxTreeNode node, Context ctx) {
        LexToken token = node.getToken();
        String text = token.getText();

        switch ((NagisaLexTokenType) token.getType()) {
            case LITERAL_INTEGER:
                return new Expr(IdentifierType.INTEGER, ExprOperator.IntegerLiteral, text);
            case LITERAL_STRING:
                return new Expr(IdentifierType.STRING, ExprOperator.StringLiteral, text);
            case IDENTIFIER:
                VariableIdentifierInfo info = ctx.registry.queryVariableInfo(text);
                if (info == null) {
                    ctx.errorReporter.report(node, Errors.E1001, text);
                    return null;
                }
                return new Expr(info.getType(), ExprOperator.VariableRef, text);
            default:
                ctx.errorReporter.report(node, Fatals.F0001, "unsupported param0 expr. token: " + token);
                return null;
        }
    }

    private static Expr resolveSingleParamExpr(SyntaxTreeNode node, Context ctx) {
        LexToken opToken = node.getChildren().get(0).getToken();
        Expr param = resolveExpr(node.getChildren().get(1), ctx);

        if (param == null)
            return null;

        switch ((NagisaLexTokenType) opToken.getType()) {
            case SYMBOL_SUB:
                if (param.type != IdentifierType.INTEGER)
                    ctx.errorReporter.report(node, Errors.E1101, IdentifierType.INTEGER, param.type);
                return new Expr(IdentifierType.INTEGER, ExprOperator.IntegerNegative, opToken.getText(), param);
            case SYMBOL_NOT:
                if (param.type != IdentifierType.STRING)
                    ctx.errorReporter.report(node, Errors.E1101, IdentifierType.BOOLEAN, param.type);
                return new Expr(IdentifierType.BOOLEAN, ExprOperator.BooleanNot, opToken.getText(), param);
            default:
                ctx.errorReporter.report(node, Fatals.F0001, "unsupported param1 expr. token: " + opToken);
                return null;
        }
    }

    private static Expr resolveDoubleParamExpr(SyntaxTreeNode node, Context ctx) {
        Expr left = resolveExpr(node.getChildren().get(0), ctx);
        LexToken opToken = node.getChildren().get(1).getToken();
        Expr right = resolveExpr(node.getChildren().get(2), ctx);

        if (left == null || right == null)
            return null;

        // 找到与当前的运算符、左表达式类型、右表达式类型都匹配的运算符
        for (OperatorInfo info : OPERATORS)
            if (info.opTokenType == opToken.getType() && info.leftType == left.type && info.rightType == right.type)
                return new Expr(info.resultType, info.operator, opToken.getText(), left, right);

        // 找不到，报错
        ctx.errorReporter.report(node, Errors.E1102, left.type, right.type, opToken.getText());
        return null;
    }

    private static Expr resolveParentheseExpr(SyntaxTreeNode node, Context ctx) {
        Expr expr = resolveExpr(node.getChildren().get(1), ctx);
        if (expr == null)
            return null;

        // 如果expr是字面量则把括号去掉（因为毫无意义）
        if (expr.children.isEmpty())
            return expr;

        return new Expr(expr, true);
    }

    private static Expr resolveFunctionCallExpr(SyntaxTreeNode node, Context ctx) {
        // 函数名
        String name = node.getChildren().get(0).getToken().getText();
        // 参数列表
        List<SyntaxTreeNode> argNodes = node.getChildren().get(2).getChildren();
        List<Expr> args = argNodes.stream()
                .map(n -> "RestArgument".equals(n.getRuleName()) ? n.getChildren().get(1) : n)
                .map(n -> resolveExpr(n, ctx))
                .collect(Collectors.toList());

        FunctionIdentifierInfo info = ctx.registry.queryFunctionInfo(name);
        if (info == null) {
            ctx.errorReporter.report(node, Errors.E1003, name);
            return null;
        }

        /*
         * 进行一些检查
         */

        // 不能以表达式的方式调用方法
        if (info.getType() == IdentifierType.VOID)
            ctx.errorReporter.report(node, Errors.E2003, info.getName());

        // 检查形参和实参的数量是否匹配
        if (args.size() != info.getParameters().size())
            ctx.errorReporter.report(node, Errors.E2004, info.getName(), info.getParameters().size(), args.size());
        else
            // 检查形参和实参的类型是否匹配
            for (int i = 0; i < args.size(); i++)
                if (args.get(i).type != info.getParameters().get(i).getType())
                    ctx.errorReporter.report(node, Errors.E2005, info.getName(), i, info.getParameters().get(i).getName(), info.getParameters().get(i).getType(), args.get(i).type);

        return new Expr(info.getType(), ExprOperator.FunctionInvocation, info.getName(), args);
    }

    private static class OperatorInfo {
        private final LexTokenType opTokenType;
        private final IdentifierType leftType;
        private final IdentifierType rightType;

        private final ExprOperator operator;
        private final IdentifierType resultType;

        OperatorInfo(LexTokenType opTokenType, IdentifierType leftType, IdentifierType rightType, ExprOperator operator, IdentifierType resultType) {
            this.opTokenType = opTokenType;
            this.leftType = leftType;
            this.rightType = rightType;
            this.operator = operator;
            this.resultType = resultType;
        }

    }

    private static final List<OperatorInfo> OPERATORS = Arrays.asList(
            new OperatorInfo(NagisaLexTokenType.SYMBOL_ADD, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerAdd, IdentifierType.INTEGER),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_SUB, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerSub, IdentifierType.INTEGER),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_MUL, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerMul, IdentifierType.INTEGER),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_DIV, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerDiv, IdentifierType.INTEGER),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_MOD, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerMod, IdentifierType.INTEGER),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_ADD, IdentifierType.STRING, IdentifierType.STRING, ExprOperator.StringAdd, IdentifierType.STRING),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_EQ, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerEq, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_NEQ, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerNeq, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_GT, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_GTE, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_LT, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_LTE, IdentifierType.INTEGER, IdentifierType.INTEGER, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_EQ, IdentifierType.STRING, IdentifierType.STRING, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_NEQ, IdentifierType.STRING, IdentifierType.STRING, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_GT, IdentifierType.STRING, IdentifierType.STRING, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_GTE, IdentifierType.STRING, IdentifierType.STRING, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_LT, IdentifierType.STRING, IdentifierType.STRING, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_LTE, IdentifierType.STRING, IdentifierType.STRING, ExprOperator.IntegerAdd, IdentifierType.BOOLEAN),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_EQ, IdentifierType.BOOLEAN, IdentifierType.BOOLEAN, ExprOperator.BooleanEq, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_NEQ, IdentifierType.BOOLEAN, IdentifierType.BOOLEAN, ExprOperator.BooleanNeq, IdentifierType.BOOLEAN),

            new OperatorInfo(NagisaLexTokenType.SYMBOL_AND, IdentifierType.BOOLEAN, IdentifierType.BOOLEAN, ExprOperator.BooleanAnd, IdentifierType.BOOLEAN),
            new OperatorInfo(NagisaLexTokenType.SYMBOL_OR, IdentifierType.BOOLEAN, IdentifierType.BOOLEAN, ExprOperator.BooleanOr, IdentifierType.BOOLEAN)
            );
}