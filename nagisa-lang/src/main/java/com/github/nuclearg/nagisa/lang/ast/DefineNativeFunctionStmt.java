package com.github.nuclearg.nagisa.lang.ast;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.github.nuclearg.nagisa.lang.lexer.NagisaLexDefinition.NagisaLexTokenType;
import com.github.nuclearg.nagisa.lang.parser.SyntaxTreeNode;

/**
 * 调用方法的语句
 * 
 * @author ng
 *
 */
public class DefineNativeFunctionStmt extends Stmt {
    /**
     * 是否返回void，这表示是一个方法
     */
    private final boolean isVoid;
    /**
     * 要调用的方法名
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

    DefineNativeFunctionStmt(SyntaxTreeNode node, Context ctx) {
        this.isVoid = node.getChildren().get(0).getToken().getType() == NagisaLexTokenType.KEYWORD_SUB;
        this.name = node.getChildren().get(1).getToken().getText();
        this.parameters = Collections.emptyList();
        this.javaClassName = "com.aaa.bb.ccc";
        this.javaMethodName = "asdfasdf";
    }

    @Override
    protected String toString(String prefix) {
        return prefix + "NATIVE " + (this.isVoid ? "SUB " : "FUNCTION ") + this.name + " (" + StringUtils.join(this.parameters, ", ") + ")" + SystemUtils.LINE_SEPARATOR
                + prefix + "    " + this.javaClassName + SystemUtils.LINE_SEPARATOR
                + prefix + "    " + this.javaMethodName + SystemUtils.LINE_SEPARATOR
                + prefix + "END " + (this.isVoid ? "SUB" : "FUNCTION") + SystemUtils.LINE_SEPARATOR;
    }
}
