package test.example.report

import test.framework.Spec

class ReportTest3 extends Spec {

    def "test#3 failure"() {

        when:
        def i = 1

        then:
        i == 2
    }

    def "test#3 success"() {

        when:
        def i = 1

        then:
        i == 1
    }
}