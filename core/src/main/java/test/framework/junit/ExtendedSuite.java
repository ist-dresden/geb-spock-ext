package test.framework.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import test.framework.report.Reporting;

import java.util.List;

public class ExtendedSuite extends Suite {

    public ExtendedSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }

    public ExtendedSuite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
        super(builder, classes);
    }

    protected ExtendedSuite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(klass, suiteClasses);
    }

    protected ExtendedSuite(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(builder, klass, suiteClasses);
    }

    protected ExtendedSuite(Class<?> klass, List<Runner> runners) throws InitializationError {
        super(klass, runners);
    }

    public Description getDescription() {
        setUp();
        return super.getDescription();
    }

    public void run(RunNotifier notifier) {
        setUp();
        super.run(notifier);
        tearDown();
    }

    protected void setUp() {
        Reporting.setUp(this);
    }

    protected void tearDown() {
        Reporting.tearDown(this);
    }
}
