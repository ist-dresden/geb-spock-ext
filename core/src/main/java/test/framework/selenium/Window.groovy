package test.framework.selenium

import geb.Browser
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver

class Window {

    private final WebDriver.Window _window;

    Window(Browser browser) {
        _window = browser.driver.manage().window();
    }

    void size(int width, int height) {
        _window.setSize(new Dimension(width, height))
    }
}
