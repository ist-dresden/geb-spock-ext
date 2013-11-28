import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

seleniumHub = "http://172.16.218.134:4441/wd/hub"

environments {

    firefox {
        driver = {
            new FirefoxDriver()
        }
    }

    hub_win_ie {
        driver = {
            new RemoteWebDriver(new URL(seleniumHub), DesiredCapabilities.internetExplorer())
        }
    }

    hub_firefox {
        println "hub_firefox..."
        driver = {
            println "driver: hub_firefox"
            new RemoteWebDriver(new URL(seleniumHub), DesiredCapabilities.firefox())
        }
    }

    hub_chrome {
        driver = {
            new RemoteWebDriver(new URL(seleniumHub), DesiredCapabilities.chrome())
        }
    }

    hub_safari {
        driver = {
            new RemoteWebDriver(new URL(seleniumHub), DesiredCapabilities.safari())
        }
    }

    hub_iphone {
        driver = {
            new RemoteWebDriver(new URL(seleniumHub), DesiredCapabilities.iphone())
        }
    }
}
