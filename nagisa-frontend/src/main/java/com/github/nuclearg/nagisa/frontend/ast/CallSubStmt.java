package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;
import com.github.nuclearg.nagisa.frontend.symbol.FunctionSymbol;
import com.github.nuclearg.nagisa.frontend.symbol.TypeSymbol;

/**
 * 调用方法的语句
 * 
 * @author ng
 *
 */
public final class CallSubStmt extends Stmt {
    /**
     * 要调用的方法名
     */
    private final String name;
    /**
     * 参数列表
     */
    private final List<Expr> arguments;

    CallSubStmt(SyntaxTreeNode node, Context ctx) {
        // 方法名
        String name = node.getChildren().get(0).getToken().getText();
        // 参数列表
        List<SyntaxTreeNode> argNodes = node.getChildren().get(1).getChildren();
        List<Expr> arguments = argNodes.stream()
                .map(n -> "RestArgument".equals(n.getRuleName()) ? n.getChildren().get(1) : n)
                .map(n -> Expr.buildExpr(n, ctx))
                .collect(Collectors.toList());
        // 参数类型列表
        List<TypeSymbol> argTypes = arguments.stream()
                .map(a -> a.getType())
                .collect(Collectors.toList());

        FunctionSymbol function = ctx.getRegistry().lookupFunctionSymbol(name, argTypes, false, node);
        if (function != null)
            name = function.getName();

        this.name = name;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    /** 要调用的方法名 */
    public String getName() {
        return this.name;
    }

    /** 参数列表 */
    public List<Expr> getArguments() {
        return this.arguments;
    }

    @Override
    protected String toString(String prefix) {
        return prefix + this.name + " " + StringUtils.join(this.arguments, ", ");
    }

}
