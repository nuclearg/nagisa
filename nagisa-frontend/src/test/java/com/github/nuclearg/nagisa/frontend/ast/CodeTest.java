package com.github.nuclearg.nagisa.frontend.ast;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.github.nuclearg.nagisa.frontend.NagisaFrontend;
import com.github.nuclearg.nagisa.frontend.NagisaLoadResult;
import com.github.nuclearg.nagisa.frontend.NagisaLoadSource;

public class CodeTest {
    @Test
    public void test() {
        String code = "function a() as integer\r\nend function\r\n";

        NagisaLoadSource source = new NagisaLoadSource(code, "CodeTest");

        NagisaLoadResult result = NagisaFrontend.loadProgram(Arrays.asList(source));

        Assert.assertTrue(result.isSuccess());

        System.out.println(result.getProgram().getMainUnit());
    }
}
