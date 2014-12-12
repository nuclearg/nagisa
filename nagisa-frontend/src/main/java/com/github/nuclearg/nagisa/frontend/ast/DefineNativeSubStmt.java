package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 本地方法定义
 * 
 * @author ng
 *
 */
public final class DefineNativeSubStmt extends DefineFunctionStmtBase {
    /**
     * 对应的java类名
     */
    private final String javaClassName;
    /**
     * 对应的java方法名
     */
    private final String javaMethodName;

    DefineNativeSubStmt(SyntaxTreeNode node, Context ctx) {
        super(node, ctx);

        String javaClassName = node.getChildren().get(6).getToken().getText();
        javaClassName = javaClassName.substring(1, javaClassName.length() - 1);
        String javaMethodName = node.getChildren().get(8).getToken().getText();
        javaMethodName = javaMethodName.substring(1, javaMethodName.length() - 1);

        this.javaClassName = javaClassName;
        this.javaMethodName = javaMethodName;
    }

    /** 对应的java类名 */
    public String getJavaClassName() {
        return this.javaClassName;
    }

    /** 对应的java方法名 */
    public String getJavaMethodName() {
        return this.javaMethodName;
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "NATIVESUB " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ")" + SystemUtils.LINE_SEPARATOR
                + prefix + "    \"" + this.javaClassName + "\"" + SystemUtils.LINE_SEPARATOR
                + prefix + "    \"" + this.javaMethodName + "\"" + SystemUtils.LINE_SEPARATOR
                + prefix + "END SUB" + SystemUtils.LINE_SEPARATOR;
    }
}
