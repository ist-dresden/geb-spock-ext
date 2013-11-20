package test.framework.geb

import geb.Browser
import geb.navigator.Navigator
import geb.navigator.NonEmptyNavigator
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

class AdaptedNavigator extends NonEmptyNavigator {

    AdaptedNavigator(Browser browser, Collection<? extends WebElement> contextElements) {
        super(browser, contextElements)
    }

    Navigator mouseover() {
        Actions actions = new Actions(browser.driver)
        actions.moveToElement(contextElements.first()).build().perform()
        this
    }
}
