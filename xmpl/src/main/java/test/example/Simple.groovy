package test.example

import test.framework.Spec

class Simple extends Spec {

    def "google"() {

        when:
        go "http://www.google.de"

        then:
        page.title == "Google"
    }
}