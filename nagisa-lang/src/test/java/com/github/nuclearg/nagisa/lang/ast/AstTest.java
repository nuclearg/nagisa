package com.github.nuclearg.nagisa.lang.ast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.github.nuclearg.nagisa.lang.parser.NagisaSyntaxDefinition;
import com.github.nuclearg.nagisa.lang.util.InputStreamUtils;

@RunWith(AstTest.class)
public class AstTest extends ParentRunner<File> {
    public AstTest(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected List<File> getChildren() {
        try {
            File dir = new File(this.getClass().getClassLoader().getResource("ast").toURI());
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
                InputStream is = new FileInputStream(child);

                String code = InputStreamUtils.read(is, Charset.forName("utf-8"));

                CompilationUnit cu = new CompilationUnit(NagisaSyntaxDefinition.parse(code));

                System.out.println(cu);
            }
        };
        this.runLeaf(statement, this.describeChild(child), notifier);
    }
}
