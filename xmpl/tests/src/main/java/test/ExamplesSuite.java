package test;

import test.framework.SpecSuite;
import org.junit.runners.Suite;
import test.example.TestcaseOne;
import test.example.TestcaseTwo;
import test.example.report.ReportSuite;

@Suite.SuiteClasses({TestcaseOne.class, TestcaseTwo.class, ReportSuite.class})
public class ExamplesSuite extends SpecSuite {

}
