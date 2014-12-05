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
public class DefineFunctionStmt extends Stmt {
    /**
     * 是否不带返回类型
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
     * 函数体
     */
    private final List<Stmt> stmts;

    DefineFunctionStmt(SyntaxTreeNode node, Context ctx) {
        this.isVoid = node.getChildren().get(0).getToken().getType() == NagisaLexTokenType.KEYWORD_SUB;
        this.name = node.getChildren().get(1).getToken().getText();
        this.parameters = Collections.emptyList();
        this.stmts = Stmt.resolveStmts(node.getChildren().get(6).getChildren(), ctx);
    }

    @Override
    protected String toString(String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(this.isVoid ? "SUB" : "FUNCTION").append(" ").append(this.name).append(" (").append(StringUtils.join(this.parameters, ", ")).append(")").append(SystemUtils.LINE_SEPARATOR);
        this.stmts.stream().forEach(s -> builder.append(prefix).append(s.toString(prefix + "  ")));
        builder.append(prefix).append("END ").append(this.isVoid ? "SUB" : "FUNCTION").append(SystemUtils.LINE_SEPARATOR);
        return builder.toString();
    }
}
