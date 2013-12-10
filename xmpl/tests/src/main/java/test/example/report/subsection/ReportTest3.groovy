package test.example.report.subsection

import test.framework.Spec

class ReportTest3 extends Spec {

    def "test#3 failure"() {

        when:
        def i = 1

        then:
        i == 2
    }

    def "test#3 success #1"() {

        when:
        def i = 1

        then:
        i == 1
    }

    def "test#3 success #2"() {

        when:
        def i = 2

        then:
        i == 2
    }
}