package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.error.Errors;
import com.github.nuclearg.nagisa.frontend.identifier.TypeIdentifierInfo;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

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
    private final TypeIdentifierInfo type;

    DefineVariableStmt(SyntaxTreeNode node, Context ctx) {
        this.name = node.getChildren().get(1).getToken().getText();

        String typeName = node.getChildren().get(3).getToken().getText();
        switch (typeName.toUpperCase()) {
            case "INTEGER":
                this.type = TypeIdentifierInfo.INTEGER;
                break;
            case "STRING":
                this.type = TypeIdentifierInfo.STRING;
                break;
            case "BOOLEAN":
                this.type = TypeIdentifierInfo.BOOLEAN;
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
