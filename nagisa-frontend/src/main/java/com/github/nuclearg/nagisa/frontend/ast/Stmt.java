package com.github.nuclearg.nagisa.frontend.ast;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;

import java.lang.reflect.Constructor;
import java.util.Collections;
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

    Stmt() {
        // 禁止这个包外面的类继承这个类
    }

    /**
     * 根据语法节点构建一系列语句，会将空语句过滤掉
     * 
     * @param nodes
     *            表示一系列语句的语法节点
     * @param ctx
     *            上下文
     * @return 语句列表，不可变列表
     */
    static List<Stmt> buildStmts(List<SyntaxTreeNode> nodes, Context ctx) {
        return Collections.unmodifiableList(nodes.stream()
                .map(n -> buildStmt(n, ctx))
                .filter(stmt -> !(stmt instanceof EmptyStmt))
                .collect(Collectors.toList()));
    }

    /**
     * 根据语法节点构建一条语句
     * 
     * @param node
     *            表示语句的语法节点
     * @param ctx
     *            上下文
     * @return 语句
     */
    private static Stmt buildStmt(SyntaxTreeNode node, Context ctx) {
        // 尝试直接创建一个与语法规则名称匹配的类实例
        try {
            String ruleName = node.getRuleName();

            // 如果是第一遍，则只解析函数签名
            if (ctx.getPhase() == ScanPhase.ScanDeclaration) {
                switch (ruleName) {
                    case "DefineFunctionStmt":
                    case "DefineSubStmt":
                    case "DefineNativeFunctionStmt":
                    case "DefineNativeSubStmt":
                        ruleName = "DefineFunctionPhase1Stmt";
                        break;
                    default:
                        // 第一遍，对于函数定义语句之外的语句全部返回空
                        return new EmptyStmt(node, ctx);
                }
            }

            String stmtClsName = Stmt.class.getPackage().getName() + "." + ruleName;
            Class<?> cls = Class.forName(stmtClsName);
            Class<? extends Stmt> stmtCls = cls.asSubclass(Stmt.class);

            Constructor<? extends Stmt> constructor = stmtCls.getDeclaredConstructor(SyntaxTreeNode.class, Context.class);

            return constructor.newInstance(node, ctx);
        } catch (Exception ex) {
            ctx.getErrorReporter().report(node, Fatals.F0001, ExceptionUtils.getStackTrace(ex));
            return null;
        }
    }

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
     * 将一系列语句输出为字符串形式
     * 
     * @param stmts
     *            语句列表
     * @param prefix
     *            前缀
     * @return 语句的字符串形式
     */
    static String toString(List<Stmt> stmts, String prefix) {
        StringBuilder builder = new StringBuilder();

        stmts.forEach(stmt -> builder.append(prefix).append(stmt).append(LN));

        return builder.toString();
    }
}
