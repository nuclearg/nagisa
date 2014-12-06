package com.github.nuclearg.nagisa.frontend.ast;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.identifier.FunctionIdentifierInfo;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

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
        this.name = node.getChildren().get(0).getToken().getText();
        // 参数列表
        List<SyntaxTreeNode> argNodes = node.getChildren().get(1).getChildren();
        this.arguments = argNodes.stream()
                .map(n -> "RestArgument".equals(n.getRuleName()) ? n.getChildren().get(1) : n)
                .map(n -> Expr.resolveExpr(n, ctx))
                .collect(Collectors.toList());

        FunctionIdentifierInfo info = ctx.registry.queryFunctionInfo(name);
        if (info == null)
            ctx.errorReporter.report(node, Errors.E1003, name);
        else {
            // 检查形参和实参的数量是否匹配
            if (this.arguments.size() != info.getParameters().size())
                ctx.errorReporter.report(node, Errors.E2004, info.getName(), info.getParameters().size(), this.arguments.size());
            else
                // 检查形参和实参的类型是否匹配
                for (int i = 0; i < this.arguments.size(); i++)
                    if (this.arguments.get(i).getType() != info.getParameters().get(i).getType())
                        ctx.errorReporter.report(node, Errors.E2005, info.getName(), i, info.getParameters().get(i).getName(), info.getParameters().get(i).getType(), this.arguments.get(i).getType());
        }
    }

    @Override
    protected String toString(String prefix) {
        return prefix + this.name + " " + StringUtils.join(this.arguments, ", ");
    }

}
