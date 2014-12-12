package com.github.nuclearg.nagisa.interceptor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.math.NumberUtils;

import com.github.nuclearg.nagisa.frontend.ast.Expr;
import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;

/**
 * 表达式的解释器
 * 
 * @author ng
 *
 */
final class ExprInterceptor {
    /**
     * 表达式
     */
    private final Expr expr;
    /**
     * 子表达式解释器列表
     */
    private final List<ExprInterceptor> children;

    private ExprInterceptor(Expr expr) {
        this.expr = expr;
        this.children = Collections.unmodifiableList(StreamSupport.stream(expr.getChildren().spliterator(), false)
                .map(e -> new ExprInterceptor(e))
                .collect(Collectors.toList()));
    }

    Value eval(Context ctx) {
        List<Value> values = this.children.stream()
                .map(e -> e.eval(ctx))
                .collect(Collectors.toList());

        long int0 = values.size() > 0 && values.get(0).getType() == TypeIdentifierInfo.INTEGER ? (Long) values.get(0).getValue() : 0;
        long int1 = values.size() > 1 && values.get(1).getType() == TypeIdentifierInfo.INTEGER ? (Long) values.get(1).getValue() : 0;
        String str0 = values.size() > 0 && values.get(0).getType() == TypeIdentifierInfo.STRING ? (String) values.get(0).getValue() : null;
        String str1 = values.size() > 1 && values.get(1).getType() == TypeIdentifierInfo.STRING ? (String) values.get(1).getValue() : null;
        boolean bool0 = values.size() > 0 && values.get(0).getType() == TypeIdentifierInfo.BOOLEAN ? (Boolean) values.get(0).getValue() : false;
        boolean bool1 = values.size() > 1 && values.get(0).getType() == TypeIdentifierInfo.BOOLEAN ? (Boolean) values.get(1).getValue() : false;

        switch (this.expr.getOperator()) {
            case IntegerLiteral:
                return new Value(NumberUtils.toLong(this.expr.getText()));
            case StringLiteral:
                return new Value(this.expr.getText().substring(1, this.expr.getText().length() - 1));
            case VariableRef:
                return ctx.getVariableValue(this.expr.getText());

            case IntegerNegative:
                return new Value(0 - int0);
            case BooleanNot:
                return new Value(!bool0);

            case IntegerAdd:
                return new Value(int0 + int1);
            case IntegerSub:
                return new Value(int0 - int1);
            case IntegerMul:
                return new Value(int0 * int1);
            case IntegerDiv:
                return new Value(int0 / int1);
            case IntegerMod:
                return new Value(int0 % int1);

            case IntegerEq:
                return new Value(int0 == int1);
            case IntegerNeq:
                return new Value(int0 != int1);
            case IntegerGt:
                return new Value(int0 > int1);
            case IntegerGte:
                return new Value(int0 >= int1);
            case IntegerLt:
                return new Value(int0 < int1);
            case IntegerLte:
                return new Value(int0 <= int1);

            case StringAdd:
                return new Value(str0 + str1);

            case StringEq:
                return new Value(str0.equals(str1));
            case StringNeq:
                return new Value(!str0.equals(str1));
            case StringGt:
                return new Value(str0.compareTo(str1) > 0);
            case StringGte:
                return new Value(str0.compareTo(str1) >= 0);
            case StringLt:
                return new Value(str0.compareTo(str1) < 0);
            case StringLte:
                return new Value(str0.compareTo(str1) <= 0);

            case BooleanEq:
                return new Value(bool0 == bool1);
            case BooleanNeq:
                return new Value(bool0 != bool1);
            case BooleanAnd:
                return new Value(bool0 && bool1);
            case BooleanOr:
                return new Value(bool0 || bool1);

            case FunctionInvocation:
                return ctx.invokeFunction(this.expr.getText(), values);

            default:
                throw new UnsupportedOperationException("operation: " + this.expr.getOperator());
        }
    }

    @Override
    public String toString() {
        return this.expr.toString();
    }

    /**
     * 构造表达式的解释器
     * 
     * @param expr
     *            表达式
     * @return 表达式的解释器
     */
    static ExprInterceptor buildInterceptor(Expr expr) {
        return new ExprInterceptor(expr);
    }

    /**
     * 构造表达式的解释器
     * 
     * @param exprs
     *            表达式列表
     * @return 表达式列表对应的解释器列表
     */
    static List<ExprInterceptor> buildInterceptors(List<Expr> exprs) {
        return exprs.stream()
                .map(e -> buildInterceptor(e))
                .collect(Collectors.toList());
    }
}
