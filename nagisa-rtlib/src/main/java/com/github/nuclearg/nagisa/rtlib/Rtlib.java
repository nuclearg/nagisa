package com.github.nuclearg.nagisa.rtlib;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * 上下文
     */
    public static final LoadResult LOAD_LIB_RESULT;

    private static final String RTLIB_BASE_PATH = StringUtils.replace(Rtlib.class.getPackage().getName(), ".", "/");

    static {
        List<String> libNames = new ArrayList<>();
        libNames.add("io/stdio.txt");
        libNames.add("lang/string.txt");
        libNames.add("lang/type.txt");

        LoadResult result = null;
        List<CompilationUnit> libs = new ArrayList<>();
        for (String libName : libNames) {
            result = loadLibResource(libName, result);
            libs.add(result.getCu());
        }

        LIBS = Collections.unmodifiableList(libs);
        LOAD_LIB_RESULT = result;
    }

    private static LoadResult loadLibResource(String path, LoadResult prevResult) {
        String code;
        try {
            code = InputStreamUtils.read(Rtlib.class.getClassLoader().getResourceAsStream(RTLIB_BASE_PATH + "/" + path), Charset.forName("utf-8"));
        } catch (IOException ex) {
            throw new IllegalStateException("加载rtlib时发生异常. path: " + path, ex);
        }

        LoadResult result = prevResult == null ? NagisaFrontend.loadCompilationUnit(code) : NagisaFrontend.loadCompilationUnit(code, prevResult);
        if (result.getCu() != null)
            return result;
        else
            throw new IllegalStateException("rtlib中发现语法错误. errors: " + result.getErrors());
    }
}
