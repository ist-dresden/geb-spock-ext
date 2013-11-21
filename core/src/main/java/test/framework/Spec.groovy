package test.framework

import geb.Configuration
import geb.spock.GebSpec
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import test.framework.geb.ExtendedConfigLoader
import test.framework.selenium.Window
import test.framework.spock.ExtendedSputnik

@RunWith(ExtendedSputnik.class)
abstract class Spec extends GebSpec {

    static final String MODE_PROPERTY = 'mode'
    static final String TEST_MODE = 'test'
    static final String LEARN_MODE = 'learn'

    protected _screenshot

    private Window _window
    private File _reportDir

    static Closure _login

    Configuration createConf() {
        new ExtendedConfigLoader(gebConfEnv, System.properties, new GroovyClassLoader(getClass().classLoader)).getConf(gebConfScript)
    }

    String getMode() {
        config.properties.get(MODE_PROPERTY, TEST_MODE)
    }

    boolean isLearnMode() {
        mode == LEARN_MODE
    }

    void login() {
        if (!_login) {
            _login = config.loginConf
            if (_login) {
                if (config.verbose) {
                    println "login ${config.username}"
                }
                _login.call(this, config.username, config.password)
            }
        }
    }

    Screenshot screenshot() {
        _screenshot = new Screenshot(this)
    }

    Screenshot getScreenshot() {
        if (_screenshot == null) {
            screenshot()
        }
        _screenshot
    }

    Window getWindow() {
        if (_window == null) {
            _window = new Window(browser)
        }
        _window
    }

    void wait(int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/shouldNotBeFound")))
        } catch (Exception e) {
            // ok
        }
    }

    void reportGroup(String path) {
        browser.reportGroup(path.replace(' ', '_'))
        report "${this.class.name} / ${path} ..."
    }

    void report(String message) {
        println message
    }

    File getReportBase() {
        new File(/*config.reportsDir ?: */ new File('test'), browser.driver.class.simpleName);
    }

    File getReportSpecDir() {
        new File(reportBase, this.class.name.replace('.', '/').toLowerCase())
    }

    File getReportDir() {
        if (!_reportDir) {
            _reportDir = new File(reportSpecDir, browser.reportGroup ?: '.')
        }
        _reportDir
    }
}
