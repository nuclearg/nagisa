package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;
import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.TAB;

import org.apache.commons.lang3.StringUtils;

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

        ctx.popLevel();
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
        return prefix + "NATIVESUB " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ")" + LN
                + prefix + TAB + "\"" + this.javaClassName + "\"" + LN
                + prefix + TAB + "\"" + this.javaMethodName + "\"" + LN
                + prefix + "END SUB" + LN;
    }
}
