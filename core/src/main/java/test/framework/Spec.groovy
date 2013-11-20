package test.framework

import geb.spock.GebSpec
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import test.framework.page.LoginPage
import test.framework.page.LoginPageCQupTo55
import test.framework.report.Reporting
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

    String getMode() {
        config.properties.get(MODE_PROPERTY, TEST_MODE)
    }

    boolean isLearnMode() {
        mode == LEARN_MODE
    }

    LoginPage login() {
        page = to LoginPageCQupTo55
        page.login()
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
        report "\n${this.class.name} / ${path} ..."
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
