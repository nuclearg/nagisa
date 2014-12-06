package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.frontend.identifier.IdentifierType;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 本地函数定义
 * 
 * @author ng
 *
 */
public class DefineNativeFunctionStmt extends Stmt {
    /**
     * 函数名
     */
    private final String name;
    /**
     * 返回类型
     */
    private final IdentifierType type;
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

    DefineNativeFunctionStmt(SyntaxTreeNode node, Context ctx) {
        this.name = node.getChildren().get(1).getToken().getText();
        this.type = null;
        this.parameters = Collections.emptyList();
        this.javaClassName = "aaa";
        this.javaMethodName = "asdfasdf";
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "NATIVEFUNCTION " + this.name + " (" + StringUtils.join(this.parameters, ", ") + ") AS " + this.type + SystemUtils.LINE_SEPARATOR
                + prefix + "    " + this.javaClassName + SystemUtils.LINE_SEPARATOR
                + prefix + "    " + this.javaMethodName + SystemUtils.LINE_SEPARATOR
                + prefix + "END FUNCTION" + SystemUtils.LINE_SEPARATOR;
    }
}
