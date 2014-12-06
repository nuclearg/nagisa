package com.github.nuclearg.nagisa.frontend.ast;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.github.nuclearg.nagisa.frontend.error.Fatals;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 语句块
 * 
 * @author ng
 *
 */
final class StmtBlock implements Iterable<Stmt> {
    /**
     * 语法树节点
     */
    private final List<SyntaxTreeNode> nodes;
    /**
     * 上下文
     */
    private final Context ctx;

    /**
     * 语句列表
     */
    private List<Stmt> stmts;

    StmtBlock(List<SyntaxTreeNode> nodes, Context ctx) {
        this.nodes = Collections.unmodifiableList(nodes);
        this.ctx = ctx;
    }

    /**
     * 遍历语句列表，初始化这些语句
     */
    void init() {
        /*
         * 对语法树进行语义解析，构造语句列表
         */
        List<Stmt> stmts = new ArrayList<>();

        for (SyntaxTreeNode node : nodes)
            // 尝试直接创建一个与语法规则名称匹配的类实例
            try {
                String ruleName = node.getRuleName();
                String stmtClsName = Stmt.class.getPackage().getName() + "." + ruleName;
                Class<?> cls = Class.forName(stmtClsName);
                Class<? extends Stmt> stmtCls = cls.asSubclass(Stmt.class);

                Constructor<? extends Stmt> constructor = stmtCls.getDeclaredConstructor(SyntaxTreeNode.class, Context.class);

                stmts.add(constructor.newInstance(node, ctx));
            } catch (Exception ex) {
                ctx.errorReporter.report(node, Fatals.F0001, ExceptionUtils.getStackTrace(ex));
            }

        /*
         * 遍历语句列表，递归初始化每条语句中包含的语句块
         */
        for (Stmt stmt : stmts)
            if (stmt instanceof StmtBlockSupported)
                ((StmtBlockSupported) stmt).initStmtBlock();

        this.stmts = stmts;
    }

    @Override
    public Iterator<Stmt> iterator() {
        return this.stmts.iterator();
    }

    @Override
    public String toString() {
        return this.toString("");
    }

    String toString(String prefix) {
        StringBuilder builder = new StringBuilder();

        for (Stmt stmt : stmts)
            builder.append(stmt.toString(prefix));

        return builder.toString();
    }

}
