package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.nuclearg.nagisa.frontend.NagisaFrontend;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * 程序
 * <p>
 * 一个程序可以包含一个或多个源文件
 * </p>
 * 
 * @author ng
 *
 */
public final class Program {
    /**
     * 主编译单元
     */
    private final CompilationUnit mainUnit;
    /**
     * 作为库的编译单元
     */
    private final List<CompilationUnit> libUnits;

    private Program(List<SyntaxTreeNode> trees, SyntaxErrorReporter errorReporter) {
        // 第一遍，解析各种声明
        Context ctx = new Context(errorReporter, ScanPhase.ScanDeclaration);

        trees.forEach(tree -> new CompilationUnit(tree, ctx));

        // 第二遍，解析所有语句
        Context ctx2 = new Context(errorReporter, ScanPhase.ScanStmt);
        List<CompilationUnit> units = trees.stream()
                .map(tree -> new CompilationUnit(tree, ctx2))
                .collect(Collectors.toList());

        this.mainUnit = units.remove(0);
        this.libUnits = Collections.unmodifiableList(units);
    }

    /** 主编译单元 */
    public CompilationUnit getMainUnit() {
        return this.mainUnit;
    }

    /** 作为库的编译单元 */
    public Iterable<CompilationUnit> getLibUnits() {
        return this.libUnits;
    }

    @Override
    public String toString() {
        return "PROGRAM WITH " + this.libUnits.size() + " LIB UNITS";
    }

    public static Program __newProgram(List<SyntaxTreeNode> trees, SyntaxErrorReporter errorReporter) {
        // 检查调用源
        if (!Thread.currentThread().getStackTrace()[2].getClassName().equals(NagisaFrontend.class.getName()))
            throw new UnsupportedOperationException("user must not invoke this method manually");

        return new Program(trees, errorReporter);
    }
}
