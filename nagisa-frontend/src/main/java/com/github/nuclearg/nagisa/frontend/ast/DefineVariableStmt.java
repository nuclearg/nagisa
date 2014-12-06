package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.identifier.IdentifierType;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 调用方法的语句
 * 
 * @author ng
 *
 */
public class DefineVariableStmt extends Stmt {
    /**
     * 变量名
     */
    private final String name;
    /**
     * 类型
     */
    private final IdentifierType type;

    DefineVariableStmt(SyntaxTreeNode node, Context ctx) {
        this.name = node.getChildren().get(1).getToken().getText();

        String typeName = node.getChildren().get(3).getToken().getText();
        switch (typeName.toUpperCase()) {
            case "INTEGER":
                this.type = IdentifierType.INTEGER;
                break;
            case "STRING":
                this.type = IdentifierType.STRING;
                break;
            case "BOOLEAN":
                this.type = IdentifierType.BOOLEAN;
                break;
            case "VOID":
                ctx.errorReporter.report(node, Errors.E1104);
                this.type = null;
                break;
            default:
                ctx.errorReporter.report(node, Errors.E1005, typeName);
                this.type = null;
        }

        if (this.type != null)
            ctx.registry.registerVariableInfo(this.name, this.type, node);
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "DIM " + this.name + " AS " + this.type + SystemUtils.LINE_SEPARATOR;
    }
}
