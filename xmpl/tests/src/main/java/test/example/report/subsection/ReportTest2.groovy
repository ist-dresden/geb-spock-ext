package test.example.report.subsection

import test.framework.Spec

class ReportTest2 extends Spec {

    def "test:2 failure"() {

        when:
        def i = 1

        then:
        i == 2
    }

    def "test:2 success"() {

        when:
        def i = 1

        then:
        i == 1
    }
}