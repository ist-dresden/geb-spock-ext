package test.example

import test.framework.Spec

class Simple extends Spec {

    def "google"() {

        /*
         uses a necessary base url (system property)
         e.g. -Dgeb.build.baseUrl=http://www.google.de/ on command line
         if you want to compare two different systems
         run test to the first system using its base url and the mode 'learn'
         e.g. -Dmode=learn -Dgeb.build.baseUrl=http://www.google-old.de/
         to make the screen template
         and then run test against the second system and use mode 'test' (default)
         e.g. -Dgeb.build.baseUrl=http://www.google-new.de/
         make the screenshot and compare this with the template (validate())
         */

        when:
        go ""

        then:
        page.title == "Google"

        screenshot().save().validate()
    }
}