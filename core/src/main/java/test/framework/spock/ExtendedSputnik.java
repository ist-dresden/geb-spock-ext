package test.framework.spock;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.spockframework.runtime.Sputnik;
import test.framework.report.Reporting;

public class ExtendedSputnik extends Sputnik {

    public ExtendedSputnik(Class<?> clazz) throws InitializationError {
        super(clazz);
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
