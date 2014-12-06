package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 方法定义
 * 
 * @author ng
 *
 */
public class DefineSubStmt extends Stmt {
    /**
     * 方法名
     */
    private final String name;
    /**
     * 形参列表
     */
    private final List<String> parameters;
    /**
     * 函数体
     */
    private final List<Stmt> stmts;

    DefineSubStmt(SyntaxTreeNode node, Context ctx) {
        this.name = node.getChildren().get(1).getToken().getText();
        this.parameters = Collections.emptyList();
        this.stmts = Stmt.resolveStmts(node.getChildren().get(6).getChildren(), ctx);
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "SUB " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ")" + SystemUtils.LINE_SEPARATOR
                + Stmt.toString(this.stmts, prefix + "    ")
                + prefix + "END SUB" + SystemUtils.LINE_SEPARATOR;
    }
}
