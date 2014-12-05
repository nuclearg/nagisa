package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.identifier.FunctionIdentifierInfo;
import com.github.nuclearg.nagisa.frontend.identifier.IdentifierType;
import com.github.nuclearg.nagisa.frontend.identifier.VariableIdentifierInfo;
import com.github.nuclearg.nagisa.frontend.lexer.LexToken;
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
     * value的顺序为integer、string、boolean时对应的操作
     */
    private static final Map<NagisaLexTokenType, ExprOperator[]> OPERATOR_MAP;

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
                text = this.text + "(" + StringUtils.join(this.children, ", ") + ")";
                break;
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
                throw new UnsupportedOperationException("node: " + node);
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
                VariableIdentifierInfo info = ctx.registry.queryVariableInfo(text, node.getRange().getStartPosition());
                if (info == null) {
                    ctx.errorReporter.report(Errors.E1002, node.getRange().getStartPosition(), text);
                    return null;
                }
                return new Expr(info.getType(), ExprOperator.VariableRef, text);
            default:
                throw new UnsupportedOperationException(token.toString());
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
                    ctx.errorReporter.report(Errors.E1004, node.getRange().getStartPosition(), IdentifierType.INTEGER, param.type);
                return new Expr(IdentifierType.INTEGER, ExprOperator.IntegerNegative, opToken.getText(), param);
            case SYMBOL_NOT:
                if (param.type != IdentifierType.STRING)
                    ctx.errorReporter.report(Errors.E1004, node.getRange().getStartPosition(), IdentifierType.BOOLEAN, param.type);
                return new Expr(IdentifierType.BOOLEAN, ExprOperator.BooleanNot, opToken.getText(), param);
            default:
                throw new UnsupportedOperationException("token: " + opToken + ", node: " + node);
        }
    }

    private static Expr resolveDoubleParamExpr(SyntaxTreeNode node, Context ctx) {
        Expr left = resolveExpr(node.getChildren().get(0), ctx);
        LexToken opToken = node.getChildren().get(1).getToken();
        Expr right = resolveExpr(node.getChildren().get(2), ctx);

        if (left == null || right == null)
            return null;

        // 判断两个表达式的类型是否一致
        // TODO 隐式类型转换
        if (left.type != right.type) {
            ctx.errorReporter.report(Errors.E1005, node.getRange().getStartPosition(), left.type, right.type);
            return null;
        }

        // 根据表达式类型从表中获取对应的表达式运算符
        ExprOperator[] array = OPERATOR_MAP.get(opToken.getType());
        if (array == null)
            throw new UnsupportedOperationException("token: " + opToken + ", node: " + node);

        IdentifierType type = left.type;
        ExprOperator op = null;

        if (type == IdentifierType.INTEGER)
            op = array[0];
        if (type == IdentifierType.STRING)
            op = array[1];
        if (type == IdentifierType.BOOLEAN)
            op = array[2];

        if (op == null)
            ctx.errorReporter.report(Errors.E1006, node.getRange().getStartPosition(), type, opToken.getText());

        return new Expr(type, op, opToken.getText(), left, right);
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

        FunctionIdentifierInfo info = ctx.registry.queryFunctionInfo(name, node.getRange().getStartPosition());
        if (info == null)
            return null;

        // 不能以表达式的方式调用方法
        if (info.getType() == IdentifierType.VOID) {
            ctx.errorReporter.report(Errors.E2003, node.getRange().getStartPosition(), info.getName());
            return null;
        }

        // 检查形参和实参的数量是否匹配
        if (args.size() != info.getParameters().size()) {
            ctx.errorReporter.report(Errors.E2004, node.getRange().getStartPosition(), info.getName());
            return null;
        }

        // 检查形参和实参的类型是否匹配
        for (int i = 0; i < args.size(); i++)
            if (args.get(i).type != info.getParameters().get(i).getType()) {
                ctx.errorReporter.report(Errors.E2005, node.getRange().getStartPosition(), info.getName(), i, info.getParameters().get(i), args.get(i).type);
                return null;
            }

        return new Expr(info.getType(), ExprOperator.FunctionInvocation, info.getName(), args);
    }

    static {
        Map<NagisaLexTokenType, ExprOperator[]> map = new HashMap<>();

        map.put(NagisaLexTokenType.SYMBOL_ADD, new ExprOperator[] { ExprOperator.IntegerAdd, ExprOperator.StringAdd, null });
        map.put(NagisaLexTokenType.SYMBOL_SUB, new ExprOperator[] { ExprOperator.IntegerSub, null, null });
        map.put(NagisaLexTokenType.SYMBOL_MUL, new ExprOperator[] { ExprOperator.IntegerMul, null, null });
        map.put(NagisaLexTokenType.SYMBOL_DIV, new ExprOperator[] { ExprOperator.IntegerDiv, null, null });
        map.put(NagisaLexTokenType.SYMBOL_MOD, new ExprOperator[] { ExprOperator.IntegerDiv, null, null });

        map.put(NagisaLexTokenType.SYMBOL_EQ, new ExprOperator[] { ExprOperator.IntegerEq, ExprOperator.StringEq, null });
        map.put(NagisaLexTokenType.SYMBOL_NEQ, new ExprOperator[] { ExprOperator.IntegerNeq, ExprOperator.StringNeq, null });
        map.put(NagisaLexTokenType.SYMBOL_GT, new ExprOperator[] { ExprOperator.IntegerGt, ExprOperator.StringGt, null });
        map.put(NagisaLexTokenType.SYMBOL_GTE, new ExprOperator[] { ExprOperator.IntegerGte, ExprOperator.StringGte, null });
        map.put(NagisaLexTokenType.SYMBOL_LT, new ExprOperator[] { ExprOperator.IntegerLt, ExprOperator.StringLt, null });
        map.put(NagisaLexTokenType.SYMBOL_LTE, new ExprOperator[] { ExprOperator.IntegerLte, ExprOperator.StringLte, null });

        map.put(NagisaLexTokenType.SYMBOL_AND, new ExprOperator[] { null, null, ExprOperator.BooleanAnd });
        map.put(NagisaLexTokenType.SYMBOL_OR, new ExprOperator[] { null, null, ExprOperator.BooleanOr });
        map.put(NagisaLexTokenType.SYMBOL_XOR, new ExprOperator[] { null, null, ExprOperator.BooleanXor });

        OPERATOR_MAP = Collections.unmodifiableMap(map);
    }
}