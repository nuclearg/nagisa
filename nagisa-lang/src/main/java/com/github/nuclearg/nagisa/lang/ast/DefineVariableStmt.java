package com.github.nuclearg.nagisa.lang.ast;

import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.identifier.IdentifierType;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

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
        this.type = null;
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "DIM " + this.name + " AS " + this.type + SystemUtils.LINE_SEPARATOR;
    }
}
