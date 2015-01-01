package com.github.nuclearg.nagisa.frontend;

import java.util.List;
import java.util.stream.Collectors;

import com.github.nuclearg.nagisa.frontend.ast.Program;
import com.github.nuclearg.nagisa.frontend.error.SyntaxErrorReporter;
import com.github.nuclearg.nagisa.frontend.parser.NagisaSyntaxDefinition;
import com.github.nuclearg.nagisa.frontend.parser.SyntaxTreeNode;

/**
 * nagisa的编译器前端，接收源代码，产出语法树
 * 
 * @author ng
 *
 */
public class NagisaFrontend {
    /**
     * 加载由一系列源代码表示的程序
     * 
     * @param sources
     * @return
     */
    public static NagisaLoadResult loadProgram(List<NagisaLoadSource> sources) {
        if (sources == null || sources.isEmpty())
            throw new IllegalArgumentException("sources is empty");

        // 语法解析
        SyntaxErrorReporter errorReporter = new SyntaxErrorReporter();
        List<SyntaxTreeNode> trees = sources.stream()
                .map(source -> NagisaSyntaxDefinition.parse(source.getCode(), source.getFilename(), errorReporter))
                .collect(Collectors.toList());

        if (errorReporter.hasErrors())
            return new NagisaLoadResult(errorReporter.getErrors());

        // 语义解析
        Program program = Program.__newProgram(trees, errorReporter);
        if (errorReporter.hasErrors())
            return new NagisaLoadResult(errorReporter.getErrors());

        return new NagisaLoadResult(program);
    }
}
