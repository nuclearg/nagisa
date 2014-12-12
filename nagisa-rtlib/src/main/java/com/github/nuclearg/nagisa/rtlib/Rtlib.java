package com.github.nuclearg.nagisa.rtlib;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.frontend.ast.CompilationUnit;
import com.github.nuclearg.nagisa.frontend.ast.NagisaFrontend;
import com.github.nuclearg.nagisa.frontend.ast.NagisaFrontend.LoadResult;
import com.github.nuclearg.nagisa.frontend.util.InputStreamUtils;

/**
 * 运行库列表
 * 
 * @author ng
 *
 */
public final class Rtlib {
    /**
     * 运行库列表
     */
    public static final List<CompilationUnit> LIBS;

    private static final String RTLIB_BASE_PATH = StringUtils.replace(Rtlib.class.getPackage().getName(), ".", "/");

    static {
        List<String> libs = new ArrayList<>();
        libs.add("io/stdio.txt");

        LIBS = Collections.unmodifiableList(libs.stream()
                .map(path -> loadLibResource(path))
                .collect(Collectors.toList()));
    }

    private static CompilationUnit loadLibResource(String path) {
        String text;
        try {
            text = InputStreamUtils.read(Rtlib.class.getClassLoader().getResourceAsStream(RTLIB_BASE_PATH + "/" + path), Charset.forName("utf-8"));
        } catch (IOException ex) {
            throw new IllegalStateException("加载rtlib时发生异常. path: " + path, ex);
        }

        LoadResult result = NagisaFrontend.loadCompilationUnit(text);
        if (result.getCu() != null)
            return result.getCu();
        else
            throw new IllegalStateException("rtlib中发现语法错误. errors: " + result.getErrors());
    }
}
