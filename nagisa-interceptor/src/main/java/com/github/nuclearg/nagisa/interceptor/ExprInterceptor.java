package com.github.nuclearg.nagisa.interceptor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.math.NumberUtils;

import com.github.nuclearg.nagisa.frontend.ast.Expr;
import com.github.nuclearg.nagisa.frontend.ast.ExprOperator;

/**
 * 表达式的解释器
 * 
 * @author ng
 *
 */
class ExprInterceptor {
    /**
     * 表达式
     */
    private final ExprOperator operator;
    /**
     * 表达式字面量
     */
    private final String text;
    /**
     * 子表达式解释器列表
     */
    private final List<ExprInterceptor> children;

    private ExprInterceptor(Expr expr) {
        this.operator = expr.getOperator();
        this.text = expr.getText();
        this.children = Collections.unmodifiableList(StreamSupport.stream(expr.getChildren().spliterator(), false)
                .map(e -> new ExprInterceptor(e))
                .collect(Collectors.toList()));
    }

    Value eval(Context ctx) {
        List<Value> values = this.children.stream().map(e -> e.eval(ctx)).collect(Collectors.toList());
        long int0 = values.size() > 0 ? values.get(0).getIntegerValue() : 0;
        long int1 = values.size() > 1 ? values.get(1).getIntegerValue() : 0;
        String str0 = values.size() > 0 ? values.get(0).getStringValue() : null;
        String str1 = values.size() > 1 ? values.get(1).getStringValue() : null;
        boolean bool0 = values.size() > 0 ? values.get(0).getBooleanValue() : false;
        boolean bool1 = values.size() > 1 ? values.get(1).getBooleanValue() : false;

        switch (this.operator) {
            case IntegerLiteral:
                return new Value(NumberUtils.toLong(this.text));
            case StringLiteral:
                return new Value(this.text);
            case VariableRef:
                return new Value(ctx.getIntegerVariableValue(this.text));

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

            case BooleanAnd:
                return new Value(bool0 && bool1);
            case BooleanOr:
                return new Value(bool0 || bool1);
            case BooleanXor:
                return new Value(bool0 != bool1);

            case FunctionInvocation:
                return null;

            default:
                throw new UnsupportedOperationException("operation: " + this.operator);
        }
    }

    static ExprInterceptor buildInterceptor(Expr expr) {
        return new ExprInterceptor(expr);
    }
}
