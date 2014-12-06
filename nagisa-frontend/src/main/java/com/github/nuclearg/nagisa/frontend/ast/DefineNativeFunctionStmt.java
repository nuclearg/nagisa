package com.github.nuclearg.nagisa.frontend.ast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 本地函数定义
 * 
 * @author ng
 *
 */
public final class DefineNativeFunctionStmt extends DefineFunctionStmtBase {
    /**
     * 对应的java类名
     */
    private final String javaClassName;
    /**
     * 对应的java方法名
     */
    private final String javaMethodName;

    DefineNativeFunctionStmt(SyntaxTreeNode node, Context ctx) {
        super(node, ctx);

        this.javaClassName = "aaa";
        this.javaMethodName = "asdfasdf";
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "NATIVEFUNCTION " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ") AS " + this.type + SystemUtils.LINE_SEPARATOR
                + prefix + "    \"" + this.javaClassName + "\"" + SystemUtils.LINE_SEPARATOR
                + prefix + "    \"" + this.javaMethodName + "\"" + SystemUtils.LINE_SEPARATOR
                + prefix + "END FUNCTION" + SystemUtils.LINE_SEPARATOR;
    }
}
