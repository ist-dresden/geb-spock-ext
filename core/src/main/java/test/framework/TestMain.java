package test.framework;

import org.junit.runner.JUnitCore;
import test.framework.report.Reporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TestMain {

    static final Pattern PROPERTY_PATTERN = Pattern.compile("^(-D?)?([^=]+)=(.*)$");

    public static void main(String[] args) {
        System.exit(new TestMain().run(args));
    }

    private String suiteClassName;

    public int run(String[] args) {
        int exitCode = 0;
        for (String arg : args) {
            Matcher matcher = PROPERTY_PATTERN.matcher(arg);
            if (matcher.matches() && matcher.groupCount() == 3) {
                System.setProperty(matcher.group(2), matcher.group(3));
            }
        }
        JUnitCore junitCore = new JUnitCore();
        String[] classNames = Spec.getConfiguration().getSuiteClassNames();
        Reporting.setUp(this);
        for (String className : classNames) {
            Class<SpecSuite> suite = null;
            try {
                System.out.println("Run tests using class: " + className);
                suite = (Class<SpecSuite>) Class.forName(className);
                try {
                    junitCore.run(suite);
                    if (Reporting.getInstance().getResult(0).getFailureCount() > 0) {
                        exitCode = 1;
                    }
                } catch (Throwable ex) {
                    System.err.println("Exception thrown performing test '" + className + "': " + ex);
                    exitCode = Math.max(exitCode, 250);
                }
            } catch (ReflectiveOperationException roex) {
                System.err.println("Can't evaluate test class! (" + roex + ")");
                exitCode = Math.max(exitCode, 251);
            }
        }
        Reporting.tearDown(this);
        return exitCode;
    }
}
