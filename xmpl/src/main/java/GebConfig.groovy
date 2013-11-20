import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import test.framework.geb.AdaptedNavigatorFactory

driver = {
    println "default firefox"
    new FirefoxDriver()
}

reportsDir = {
    new File().getAbsolutePath() + "/test"
}

navigatorFactory = { browser ->
    new AdaptedNavigatorFactory(browser)
}

environments {

    hub_win_ie {
        println "hub_win_ie"
        driver = {
            println "remote IE via hub"
            new RemoteWebDriver(new URL(seleniumHub), DesiredCapabilities.internetExplorer())
        }
    }

    hub_firefox {
        println "hub_firefox"
        driver = {
            println "remote Firefox via hub"
            new RemoteWebDriver(new URL(seleniumHub), DesiredCapabilities.firefox())
        }
    }

    hub_safari {
        println "hub_safari"
        driver = {
            println "remote Safari via hub"
            new RemoteWebDriver(new URL(seleniumHub), DesiredCapabilities.safari())
        }
    }
}
