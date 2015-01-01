package com.github.nuclearg.nagisa.frontend.ast;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.github.nuclearg.nagisa.frontend.NagisaFrontend;
import com.github.nuclearg.nagisa.frontend.NagisaLoadResult;
import com.github.nuclearg.nagisa.frontend.NagisaLoadSource;
import com.github.nuclearg.nagisa.frontend.util.InputStreamUtils;

public class SourceTest {
    @Test
    public void test() throws Exception {
        String code = InputStreamUtils.read(this.getClass().getClassLoader().getResourceAsStream("sourcecode.txt"), Charset.forName("utf-8"));

        NagisaLoadSource source = new NagisaLoadSource(code, "CodeTest");

        NagisaLoadResult result = NagisaFrontend.loadProgram(Arrays.asList(source));

        Assert.assertTrue(result.isSuccess());

        System.out.println(result.getProgram().getMainUnit());
    }
}
