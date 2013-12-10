package test.example.report

import test.framework.Spec

class ReportTest extends Spec {

    def "test success #1"() {

        when:
        def i = 1

        then:
        i == 1
    }

    def "test failure #1"() {

        when:
        def i = 0

        then:
        i == 1
    }

    def "test success #2"() {

        when:
        def i = 2

        then:
        i == 2
    }

    def "test failure #2"() {

        when:
        def i = 0

        then:
        i == 2
    }

    def "test failure #3"() {

        when:
        def i = 0

        then:
        i == 2
    }
}