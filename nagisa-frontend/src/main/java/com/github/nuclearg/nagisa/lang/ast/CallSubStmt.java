package com.github.nuclearg.nagisa.lang.ast;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 调用方法的语句
 * 
 * @author ng
 *
 */
public class CallSubStmt extends Stmt {
    /**
     * 要调用的方法名
     */
    private final String name;
    /**
     * 参数列表
     */
    private final List<Expr> arguments;

    CallSubStmt(SyntaxTreeNode node, Context ctx) {
        this.name = node.getChildren().get(0).getToken().getText();
        this.arguments = Collections.emptyList();
    }

    @Override
    protected String toString(String prefix) {
        return prefix + this.name + " " + StringUtils.join(this.arguments, ", ") + SystemUtils.LINE_SEPARATOR;
    }

}
