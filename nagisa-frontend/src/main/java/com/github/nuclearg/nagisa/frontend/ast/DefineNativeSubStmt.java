package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 本地方法定义
 * 
 * @author ng
 *
 */
public class DefineNativeSubStmt extends Stmt {
    /**
     * 方法名
     */
    private final String name;
    /**
     * 形参列表
     */
    private final List<String> parameters;
    /**
     * 对应的java类名
     */
    private final String javaClassName;
    /**
     * 对应的java方法名
     */
    private final String javaMethodName;

    DefineNativeSubStmt(SyntaxTreeNode node, Context ctx) {
        this.name = node.getChildren().get(1).getToken().getText();
        this.parameters = Collections.emptyList();
        this.javaClassName = "aaa";
        this.javaMethodName = "asdfasdf";
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "NATIVESUB " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ")" + SystemUtils.LINE_SEPARATOR
                + prefix + "    " + this.javaClassName + SystemUtils.LINE_SEPARATOR
                + prefix + "    " + this.javaMethodName + SystemUtils.LINE_SEPARATOR
                + prefix + "END SUB" + SystemUtils.LINE_SEPARATOR;
    }
}
