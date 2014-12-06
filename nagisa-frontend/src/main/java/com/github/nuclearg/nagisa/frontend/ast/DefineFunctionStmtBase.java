package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.identifier.IdentifierType;
import com.github.nuclearg.nagisa.frontend.identifier.VariableIdentifierInfo;
import com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition.NagisaLexTokenType;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 把对方法或函数定义的语法解析部分抽出来
 * 
 * @author ng
 *
 */
abstract class DefineFunctionStmtBase extends Stmt {
    /**
     * 函数名
     */
    protected final String name;
    /**
     * 返回类型
     */
    protected final IdentifierType type;
    /**
     * 形参列表
     */
    protected final List<VariableIdentifierInfo> parameters;

    DefineFunctionStmtBase(SyntaxTreeNode node, Context ctx) {
        // 方法或函数名
        this.name = node.getChildren().get(1).getToken().getText();

        // 类型
        if (node.getChildren().get(5).getToken().getType() == NagisaLexTokenType.EOL) // 没有类型声明
            this.type = IdentifierType.VOID;
        else {
            SyntaxTreeNode typeNode = node.getChildren().get(6);
            String typeName = typeNode.getToken().getText();

            this.type = ctx.registry.queryTypeInfo(typeName);
            if (this.type == null)
                ctx.errorReporter.report(typeNode, Errors.E1005, typeName);
            if (this.type == IdentifierType.VOID)
                ctx.errorReporter.report(typeNode, Errors.E1104);
        }

        // 形参列表
        SyntaxTreeNode parametersNode = node.getChildren().get(3);
        List<VariableIdentifierInfo> parameters = parametersNode.getChildren().stream()
                .map(n -> "RestParam".equals(n.getRuleName()) ? n.getChildren().get(1) : n)
                .map(n -> {
                    String name = n.getChildren().get(0).getToken().getText();
                    SyntaxTreeNode typeNode = n.getChildren().get(2);
                    String typeName = typeNode.getToken().getText();

                    IdentifierType type = ctx.registry.queryTypeInfo(typeName);
                    if (type == null)
                        ctx.errorReporter.report(typeNode, Errors.E1005, typeName);
                    if (type == IdentifierType.VOID)
                        ctx.errorReporter.report(typeNode, Errors.E1104);

                    return new VariableIdentifierInfo(name, type);
                })
                .collect(Collectors.toList());

        this.parameters = Collections.unmodifiableList(parameters);

        ctx.registry.registerFunctionInfo(this.name, this.type, this.parameters, node);
    }
}