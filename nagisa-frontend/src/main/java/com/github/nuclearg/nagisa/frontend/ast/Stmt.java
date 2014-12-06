package com.github.nuclearg.nagisa.frontend.ast;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.github.nuclearg.nagisa.frontend.error.Fatals;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 语句
 * 
 * @author ng
 *
 */
public abstract class Stmt {

    @Override
    public String toString() {
        return this.toString("");
    }

    /**
     * 需要实现类实现的带前缀（缩进）的toString
     * 
     * @param prefix
     *            前缀
     * @return 带前缀的字符串形式
     */
    protected abstract String toString(String prefix);

    /**
     * 将一系列语法节点解析为语句列表
     * 
     * @param nodes
     *            语法节点列表
     * @param ctx
     *            上下文
     * @return 语句列表
     */
    static List<Stmt> resolveStmts(List<SyntaxTreeNode> nodes, Context ctx) {
        return nodes.stream().map(n -> resolveStmt(n, ctx)).collect(Collectors.toList());
    }

    /**
     * 将一个语法节点解析为单条语句
     * 
     * @param node
     *            语法节点
     * @param ctx
     *            上下文
     * @return 语句
     */
    private static Stmt resolveStmt(SyntaxTreeNode node, Context ctx) {
        // 尝试直接创建一个与语法规则名称匹配的类实例
        try {
            String ruleName = node.getRuleName();
            String stmtClsName = Stmt.class.getPackage().getName() + "." + ruleName;
            Class<?> cls = Class.forName(stmtClsName);
            Class<? extends Stmt> stmtCls = cls.asSubclass(Stmt.class);
            Constructor<? extends Stmt> constructor = stmtCls.getDeclaredConstructor(SyntaxTreeNode.class, Context.class);
            return constructor.newInstance(node, ctx);
        } catch (Exception ex) {
            ctx.errorReporter.report(node, Fatals.F0001, ExceptionUtils.getStackTrace(ex));
            return null;
        }
    }

    /**
     * 将一系列语句输出为字符串形式
     * 
     * @param stmts
     *            语句列表
     * @param prefix
     *            前缀
     * @return 这些语句的字符串形式
     */
    static String toString(Iterable<Stmt> stmts, String prefix) {
        StringBuilder builder = new StringBuilder();

        for (Stmt stmt : stmts)
            builder.append(stmt.toString(prefix));

        return builder.toString();
    }
}
