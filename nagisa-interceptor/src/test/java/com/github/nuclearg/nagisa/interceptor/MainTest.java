package com.github.nuclearg.nagisa.interceptor;

import java.io.File;

import org.junit.Test;

public class MainTest {
    @Test
    public void test1() throws Exception {
        test("test1.txt");
    }

    @Test
    public void test2() throws Exception {
        test("test2.txt");
    }

    private void test(String filename) throws Exception {
        File f = new File(this.getClass().getClassLoader().getResource(filename).toURI());

        String fn = f.getAbsolutePath();

        Main.main(new String[] { fn });
    }
}
