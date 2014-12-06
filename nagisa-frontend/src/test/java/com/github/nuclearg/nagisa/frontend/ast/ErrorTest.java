package com.github.nuclearg.nagisa.frontend.ast;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

@RunWith(ErrorTest.class)
public class ErrorTest extends ParentRunner<File> {
    public ErrorTest(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected List<File> getChildren() {
        try {
            File dir = new File(this.getClass().getClassLoader().getResource("error").toURI());
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
                ParseResult result = ParserUtils.parse(child);

                System.out.println(result.cu);

                // 检查是否有报错
                Assert.assertFalse(result.errors.isEmpty());

                // 检查报的那条错误是否是期望的错误
                String errorName = result.errors.get(0).getError().name();
                String expected = child.getName().substring(0, child.getName().indexOf('.'));
                Assert.assertEquals(expected, errorName);
            }
        };
        this.runLeaf(statement, this.describeChild(child), notifier);
    }
}
