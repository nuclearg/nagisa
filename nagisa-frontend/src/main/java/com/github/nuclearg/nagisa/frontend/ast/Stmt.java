package com.github.nuclearg.nagisa.frontend.ast;

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
}
