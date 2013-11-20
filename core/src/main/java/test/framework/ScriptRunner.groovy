package test.framework

import geb.Browser
import geb.binding.BindingUpdater

class ScriptRunner {

    void test(String filename) {
        test(new File(filename))
    }

    void test(File file) {
        Binding binding = new Binding()
        Browser browser = new Browser()
        BindingUpdater updater = new BindingUpdater(binding, browser)
        updater.initialize()
        try {
            GroovyShell shell = new GroovyShell(this.class.classLoader, binding)
            shell.evaluate(file)
        } finally {
            updater.remove()
        }
    }
}
