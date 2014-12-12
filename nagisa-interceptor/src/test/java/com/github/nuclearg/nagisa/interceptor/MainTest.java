package com.github.nuclearg.nagisa.interceptor;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.github.nuclearg.nagisa.frontend.ast.NagisaFrontend;
import com.github.nuclearg.nagisa.frontend.ast.NagisaFrontend.LoadResult;
import com.github.nuclearg.nagisa.frontend.util.InputStreamUtils;
import com.github.nuclearg.nagisa.rtlib.Rtlib;

@RunWith(MainTest.class)
public class MainTest extends ParentRunner<File> {
    public MainTest(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected List<File> getChildren() {
        try {
            File dir = new File(this.getClass().getClassLoader().getResource("interceptor").toURI());
            return Arrays.asList(dir.listFiles());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Description describeChild(File child) {
        return Description.createSuiteDescription(child.getName());
    }

    @Override
    protected void runChild(File child, RunNotifier notifier) {
        Statement statement = new Statement() {

            @Override
            public void evaluate() throws Throwable {
                String code = InputStreamUtils.read(new FileInputStream(child), Charset.forName("utf-8"));

                LoadResult result = NagisaFrontend.loadCompilationUnit(code, Rtlib.LOAD_LIB_RESULT);

                Assert.assertTrue(result.getErrors().isEmpty());

                NagisaInterceptor.eval(result.getCu(), new ArrayList<>(), Rtlib.LIBS);
            }
        };
        this.runLeaf(statement, this.describeChild(child), notifier);
    }
}