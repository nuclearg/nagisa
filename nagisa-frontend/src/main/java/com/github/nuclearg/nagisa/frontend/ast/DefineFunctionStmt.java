package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.identifier.IdentifierType;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 函数定义
 * 
 * @author ng
 *
 */
public class DefineFunctionStmt extends Stmt {
    /**
     * 函数名
     */
    private final String name;
    /**
     * 函数的返回类型
     */
    private final IdentifierType type;
    /**
     * 形参列表
     */
    private final List<String> parameters;
    /**
     * 函数体
     */
    private final List<Stmt> stmts;

    DefineFunctionStmt(SyntaxTreeNode node, Context ctx) {
        this.name = node.getChildren().get(1).getToken().getText();
        this.type = null;
        this.parameters = Collections.emptyList();
        this.stmts = Stmt.resolveStmts(node.getChildren().get(6).getChildren(), ctx);
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "FUNCTION " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ") AS " + this.type + SystemUtils.LINE_SEPARATOR
                + Stmt.toString(this.stmts, prefix + "    ")
                + prefix + "END FUNCTION" + SystemUtils.LINE_SEPARATOR;
    }
}
