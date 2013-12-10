package test.example.report;

import test.example.report.subsection.ReportTest2;
import test.example.report.subsection.ReportTest3;
import test.framework.SpecSuite;
import org.junit.runners.Suite;

@Suite.SuiteClasses({ReportTest.class, ReportTest2.class, ReportTest3.class, ReportTestLoop.class})
public class ReportSuite extends SpecSuite {
}
