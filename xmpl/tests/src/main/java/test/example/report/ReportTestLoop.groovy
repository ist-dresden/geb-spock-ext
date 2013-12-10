package test.example.report

import test.framework.Spec

class ReportTestLoop extends Spec {

    def "test loop #1"() {

        setup:
        println "value: ${value}"

        when:
        def i = value

        then:
        i == test

        where:
        value << [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        test << [1, 2, 3, 4, 5, 6, 7, 8, 8, 10, 11]
    }
}