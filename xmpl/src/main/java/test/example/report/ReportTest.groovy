package test.example.report

import test.framework.Spec

class ReportTest extends Spec {

    def "test failure"() {

        when:
        def i = 1

        then:
        i == 2
    }

    def "test success"() {

        when:
        def i = 1

        then:
        i == 1
    }
}