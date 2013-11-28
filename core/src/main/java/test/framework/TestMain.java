package test.framework;

import test.framework.SpecSuite;
import org.junit.runner.JUnitCore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {

    public static final Pattern PROPERTY_PATTERN = Pattern.compile("^(-D?)?([^=]+)=(.*)$");

    public static void main(String[] args) {
        for (String arg : args) {
            Matcher matcher = PROPERTY_PATTERN.matcher(arg);
            if (matcher.matches() && matcher.groupCount() == 3) {
                System.setProperty(matcher.group(2), matcher.group(3));
            }
        }
        String allTestsClassName = System.getProperty("testsuite", "TestSuite");
        System.out.println("Run tests using suite: " + allTestsClassName);
        Class<SpecSuite> testsuite = null;
        try {
            testsuite = (Class<SpecSuite>) Class.forName(allTestsClassName);
        } catch (ReflectiveOperationException roex) {
            System.err.println("Can't instantiate test suite: " + allTestsClassName);
            System.exit(253);
        }
        if (testsuite != null) {
            try {
                JUnitCore junitCore = new JUnitCore();
                junitCore.run(testsuite);
                System.exit(0);
            } catch (Throwable ex) {
                System.err.println("Exception thrown: " + ex);
                System.exit(250);
            }
        } else {
            System.err.println("No testsuite found!");
            System.exit(254);
        }
    }
}
