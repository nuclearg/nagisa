package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;
import com.github.nuclearg.nagisa.frontend.symbol.TypeSymbol;

/**
 * 变量定义语句
 * 
 * @author ng
 *
 */
public final class DefineVariableStmt extends Stmt {
    /**
     * 变量名
     */
    private final String name;
    /**
     * 类型
     */
    private final TypeSymbol type;

    DefineVariableStmt(SyntaxTreeNode node, Context ctx) {
        this.name = node.getChildren().get(1).getToken().getText();

        String typeName = node.getChildren().get(3).getToken().getText();
        this.type = ctx.getRegistry().lookupTypeSymbol(typeName, node);

        if (this.type != null)
            ctx.getRegistry().registerVariableInfo(this.name, this.type, node);
    }

    /** 变量名 */
    public String getName() {
        return this.name;
    }

    /** 类型 */
    public TypeSymbol getType() {
        return this.type;
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "DIM " + this.name + " AS " + this.type + LN;
    }
}
