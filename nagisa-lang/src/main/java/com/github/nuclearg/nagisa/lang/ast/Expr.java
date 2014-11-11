package com.github.nuclearg.nagisa.lang.ast;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.nuclearg.nagisa.lang.lexer.LexToken;
import com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition.NagisaLexTokenType;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

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
    private final ExprType type;
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

    private Expr(ExprType type, ExprOperator operator, String text) {
        this.type = type;
        this.operator = operator;
        this.text = text;
        this.children = Collections.emptyList();
        this.priorityRedefined = false;
    }

    private Expr(ExprType type, ExprOperator operator, String text, Expr child) {
        this.type = type;
        this.operator = operator;
        this.text = text;
        this.children = Arrays.asList(child);
        this.priorityRedefined = false;
    }

    private Expr(ExprType type, ExprOperator operator, String text, Expr left, Expr right) {
        this.type = type;
        this.operator = operator;
        this.text = text;
        this.children = Arrays.asList(left, right);
        this.priorityRedefined = false;
    }

    private Expr(Expr expr, boolean priorityRedefined) {
        this.type = expr.type;
        this.operator = expr.operator;
        this.text = expr.text;
        this.children = expr.children;
        this.priorityRedefined = true;
    }

    /** 表达式类型 */
    public ExprType getType() {
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
                throw new UnsupportedOperationException("children: " + this.children);
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
     * @return 表达式语法树
     */
    static Expr resolveExpr(SyntaxTreeNode node) {
        LexToken firstToken = null;
        if (node.getChildren().size() > 1)
            firstToken = node.getChildren().get(0).getToken();

        // 判断表达式有几个组成部分
        switch (node.getChildren().size()) {
            case 0:
                // 判断是否带括号
                return resolveNoneParamExpr(node);
            case 1:
                return resolveExpr(node.getChildren().get(0));
            case 2:
                // 判断是单目运算符还是双目运算符，数字运算的双目运算符只有两个child
                if (firstToken != null && (firstToken.getType() == NagisaLexTokenType.OPERATOR_SUB || firstToken.getType() == NagisaLexTokenType.OPERATOR_NOT))
                    return resolveSingleParamExpr(node);
                else
                    return resolveDoubleParamExpr(node);
            case 3:
                // 判断是双目运算符还是括号表达式
                if (firstToken != null && firstToken.getType() == NagisaLexTokenType.OPERATOR_PARENTHESE_LEFT)
                    return resolveParentheseExpr(node);
                else
                    return resolveDoubleParamExpr(node);
            default:
                throw new UnsupportedOperationException("expr size: " + node.getChildren().size() + ", node: " + node);
        }
    }

    private static Expr resolveNoneParamExpr(SyntaxTreeNode node) {
        LexToken token = node.getToken();
        String text = token.getText();

        switch ((NagisaLexTokenType) token.getType()) {
            case INTEGER:
                return new Expr(ExprType.Integer, ExprOperator.IntegerLiteral, text);
            case SYMBOL:
                return new Expr(ExprType.Integer, ExprOperator.IntegerVariableRef, text);
            case STRING:
                return new Expr(ExprType.String, ExprOperator.StringLiteral, text);
            case STRING_SYMBOL:
                return new Expr(ExprType.String, ExprOperator.StringVariableRef, text);
            default:
                throw new UnsupportedOperationException(token.toString());
        }
    }

    private static Expr resolveSingleParamExpr(SyntaxTreeNode node) {
        LexToken opToken = node.getChildren().get(0).getToken();
        Expr param = resolveExpr(node.getChildren().get(1));

        switch ((NagisaLexTokenType) opToken.getType()) {
            case OPERATOR_SUB:
                return new Expr(ExprType.Integer, ExprOperator.IntegerNegative, opToken.getText(), param);
            case OPERATOR_NOT:
                return new Expr(ExprType.Boolean, ExprOperator.BooleanNot, opToken.getText(), param);
            default:
                throw new UnsupportedOperationException("token: " + opToken + ", node: " + node);
        }
    }

    private static Expr resolveDoubleParamExpr(SyntaxTreeNode node) {
        Expr left;
        LexToken opToken;
        Expr right;

        if (node.getChildren().size() == 3) {
            // 用三个节点承载一个双目运算符
            left = resolveExpr(node.getChildren().get(0));
            opToken = node.getChildren().get(1).getToken();
            right = resolveExpr(node.getChildren().get(2));
        } else {
            // 用两个节点承载一个双目运算符，运算符位于第二个节点的第一个位置
            SyntaxTreeNode node2 = node.getChildren().get(1);
            if (node2.getChildren().size() != 2)
                throw new UnsupportedOperationException("node: " + node);

            left = resolveExpr(node.getChildren().get(0));
            opToken = node2.getChildren().get(0).getToken();
            right = resolveExpr(node2.getChildren().get(1));
        }

        // 根据表达式类型从表中获取对应的表达式运算符
        ExprOperator[] array = OPERATOR_MAP.get(opToken.getType());
        if (array == null)
            throw new UnsupportedOperationException("token: " + opToken + ", node: " + node);

        ExprType type = left.type;
        ExprOperator op;
        switch (type) {
            case Integer:
                op = array[0];
                break;
            case String:
                op = array[1];
                break;
            case Boolean:
                op = array[2];
                break;
            default:
                throw new UnsupportedOperationException(type.toString());
        }

        if (op == null)
            throw new UnsupportedOperationException("token: " + opToken + ", type: " + type + ", node: " + node);

        return new Expr(type, op, opToken.getText(), left, right);
    }

    private static Expr resolveParentheseExpr(SyntaxTreeNode node) {
        Expr expr = resolveExpr(node.getChildren().get(1));

        // 如果expr是字面量则把括号去掉（因为毫无意义）
        if (expr.children.isEmpty())
            return expr;

        return new Expr(expr, true);
    }

    static {
        Map<NagisaLexTokenType, ExprOperator[]> map = new HashMap<>();

        map.put(NagisaLexTokenType.OPERATOR_ADD, new ExprOperator[] { ExprOperator.IntegerAdd, ExprOperator.StringAdd, null });
        map.put(NagisaLexTokenType.OPERATOR_SUB, new ExprOperator[] { ExprOperator.IntegerSub, null, null });
        map.put(NagisaLexTokenType.OPERATOR_MUL, new ExprOperator[] { ExprOperator.IntegerMul, null, null });
        map.put(NagisaLexTokenType.OPERATOR_DIV, new ExprOperator[] { ExprOperator.IntegerDiv, null, null });
        map.put(NagisaLexTokenType.OPERATOR_MOD, new ExprOperator[] { ExprOperator.IntegerDiv, null, null });

        map.put(NagisaLexTokenType.OPERATOR_EQ, new ExprOperator[] { ExprOperator.IntegerEq, ExprOperator.StringEq, null });
        map.put(NagisaLexTokenType.OPERATOR_NEQ, new ExprOperator[] { ExprOperator.IntegerNeq, ExprOperator.StringNeq, null });
        map.put(NagisaLexTokenType.OPERATOR_GT, new ExprOperator[] { ExprOperator.IntegerGt, ExprOperator.StringGt, null });
        map.put(NagisaLexTokenType.OPERATOR_GTE, new ExprOperator[] { ExprOperator.IntegerGte, ExprOperator.StringGte, null });
        map.put(NagisaLexTokenType.OPERATOR_LT, new ExprOperator[] { ExprOperator.IntegerLt, ExprOperator.StringLt, null });
        map.put(NagisaLexTokenType.OPERATOR_LTE, new ExprOperator[] { ExprOperator.IntegerLte, ExprOperator.StringLte, null });

        map.put(NagisaLexTokenType.OPERATOR_AND, new ExprOperator[] { null, null, ExprOperator.BooleanAnd });
        map.put(NagisaLexTokenType.OPERATOR_OR, new ExprOperator[] { null, null, ExprOperator.BooleanOr });
        map.put(NagisaLexTokenType.OPERATOR_XOR, new ExprOperator[] { null, null, ExprOperator.BooleanXor });

        OPERATOR_MAP = Collections.unmodifiableMap(map);
    }
}