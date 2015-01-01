package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.nuclearg.nagisa.frontend.lexer.LexTokenType;
import com.github.nuclearg.nagisa.frontend.lexer.NagisaLexDefinition.NagisaLexTokenType;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;
import com.github.nuclearg.nagisa.frontend.symbol.TypeSymbol;
import com.github.nuclearg.nagisa.frontend.symbol.VariableSymbol;

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
     * 函数类型
     */
    protected final LexTokenType functionKeywordType;
    /**
     * 返回类型
     */
    protected final TypeSymbol type;
    /**
     * 形参列表
     */
    protected final List<VariableSymbol> parameters;
    /**
     * 形参类型列表
     */
    protected final List<TypeSymbol> parameterType;

    DefineFunctionStmtBase(SyntaxTreeNode node, Context ctx) {
        ctx.pushLevel(false);

        // 方法或函数名
        this.name = node.getChildren().get(1).getToken().getText();

        // 函数类型
        this.functionKeywordType = node.getChildren().get(0).getToken().getType();

        // 返回类型
        if (node.getChildren().get(5).getToken().getType() == NagisaLexTokenType.EOL) // 没有类型声明
            this.type = TypeSymbol.VOID;
        else {
            SyntaxTreeNode typeNode = node.getChildren().get(6);
            String typeName = typeNode.getToken().getText();

            this.type = ctx.getRegistry().lookupTypeSymbol(typeName, typeNode);
        }

        // 形参列表
        SyntaxTreeNode parametersNode = node.getChildren().get(3);
        List<VariableSymbol> parameters = parametersNode.getChildren().stream()
                .map(n -> "RestParam".equals(n.getRuleName()) ? n.getChildren().get(1) : n)
                .map(n -> {
                    // 取形参名称
                    SyntaxTreeNode nameNode = n.getChildren().get(0);
                    String name = nameNode.getToken().getText();

                    // 取形参类型
                    SyntaxTreeNode typeNode = n.getChildren().get(2);
                    String typeName = typeNode.getToken().getText();
                    TypeSymbol type = ctx.getRegistry().lookupTypeSymbol(typeName, parametersNode);

                    // 注册形参
                    if (type == null)
                        type = TypeSymbol.VOID;

                    ctx.getRegistry().registerVariableInfo(name, type, nameNode);
                    return new VariableSymbol(name, type);
                })
                .collect(Collectors.toList());
        this.parameters = Collections.unmodifiableList(parameters);
        this.parameterType = Collections.unmodifiableList(this.parameters.stream()
                .map(p -> p.getType())
                .collect(Collectors.toList()));
    }

    /** 函数名 */
    public String getName() {
        return this.name;
    }

    /** 返回类型 */
    public TypeSymbol getType() {
        return this.type;
    }

    /** 形参列表 */
    public List<VariableSymbol> getParameters() {
        return this.parameters;
    }
}
