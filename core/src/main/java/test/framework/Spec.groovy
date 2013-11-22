package test.framework

import geb.Configuration
import geb.spock.GebSpec
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import test.framework.config.Target
import test.framework.geb.ExtendedConfigLoader
import test.framework.selenium.Window
import test.framework.spock.ExtendedSputnik

@RunWith(ExtendedSputnik.class)
abstract class Spec extends GebSpec {

    static final String MODE_PROPERTY = 'mode'
    static final String TEST_MODE = 'test'
    static final String LEARN_MODE = 'learn'

    static Target _target

    protected _screenshot

    private Window _window
    private File _reportDir
    private boolean _loggedIn

    Configuration createConf() {
        new ExtendedConfigLoader(gebConfEnv, System.properties, new GroovyClassLoader(getClass().classLoader)).getConf(gebConfScript)
    }

    String getMode() {
        config.properties.get(MODE_PROPERTY, TEST_MODE)
    }

    boolean isLearnMode() {
        mode == LEARN_MODE
    }

    // login

    void login() {
        if (!_loggedIn) {
            if (config.loginConf) {
                if (config.verbose) {
                    println "login ${config.username}"
                }
                String currentBase = getBaseUrl()
                setBaseUrl(target.serverUrl)
                config.loginConf.call(this, config.username, config.password)
                setBaseUrl(currentBase)
            }
            _loggedIn = true
        }
    }

    void resetBrowser() {
        super.resetBrowser()
        _loggedIn = false
    }

    void setup() {
        setBaseUrl(target.getBaseUrl())
        login()
    }

    // Target and baseUrl

    Target getTarget() {
        if (!_target) {
            if (config.targetConf) {
                _target = config.targetConf.call()
            }
        }
        _target
    }

    void setBaseUrl(String path = "") {
        if (!path.matches("^http(s)?://.*")) {
            path = target.getBaseUrl(path)
        }
        super.setBaseUrl(path)
    }

    // screenshots

    Screenshot screenshot() {
        _screenshot = new Screenshot(this)
    }

    Screenshot getScreenshot() {
        if (_screenshot == null) {
            screenshot()
        }
        _screenshot
    }

    // selenium (Geb extensions)

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

    // reporting

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
