package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;

import com.github.nuclearg.nagisa.frontend.error.Errors;
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
        switch (typeName.toUpperCase()) {
            case "INTEGER":
                this.type = TypeSymbol.INTEGER;
                break;
            case "STRING":
                this.type = TypeSymbol.STRING;
                break;
            case "BOOLEAN":
                this.type = TypeSymbol.BOOLEAN;
                break;
            case "VOID":
                ctx.getErrorReporter().report(node, Errors.E1104);
                this.type = null;
                break;
            default:
                ctx.getErrorReporter().report(node, Errors.E1005, typeName);
                this.type = null;
        }

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
