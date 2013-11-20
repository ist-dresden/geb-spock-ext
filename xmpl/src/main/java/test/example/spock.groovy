/**
 * a script with more than one specification - each specification is a test case
 * and must be referenced from a test suite by it's class name.
 */
package test.example

import test.framework.Spec

class TestcaseOne extends Spec {

    def "google"() {

        when: "we open the 'Google' search page"
        go "http://www.google.de"

        then: "the title must be 'Google'"
        page.title == "Google"
    }
}

class TestcaseTwo extends Spec {

    def "heise (failure)"() {

        when: "we open the landig page of 'heise.de'"
        go "http://www.heise.de"

        then: "the test for the title should fail..."
        page.title == "Heise"
    }

    def "wikipedia"() {

        when: "we opne Wikipedis"
        go "http://www.wikipedia.de"

        then: "the page title should contain 'Wikipedia'"
        page.title =~ /Wikipedia/
    }
}