package test.example.report;

import test.framework.TestSuite;
import org.junit.runners.Suite;

@Suite.SuiteClasses({ReportTest.class, ReportTest2.class, ReportTest3.class})
public class ReportSuite extends TestSuite {
}
