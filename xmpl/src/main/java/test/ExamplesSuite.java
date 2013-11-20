package test;

import junit.framework.TestSuite;
import org.junit.runners.Suite;
import test.example.Achtgrau;
import test.example.IstSite;
import test.example.LocalCQ;
import test.example.TestcaseOne;
import test.example.TestcaseTwo;

@Suite.SuiteClasses({TestcaseOne.class, TestcaseTwo.class})
public class ExamplesSuite extends TestSuite {
}
