package test.framework.geb

import geb.Browser
import geb.navigator.EmptyNavigator
import geb.navigator.Navigator
import geb.navigator.factory.DefaultInnerNavigatorFactory
import org.openqa.selenium.WebElement

class AdaptedInnerNavigatorFactory extends DefaultInnerNavigatorFactory {

    @Override
    Navigator createNavigator(Browser browser, List<WebElement> elements) {
        elements ? new AdaptedNavigator(browser, elements) : new EmptyNavigator(browser)
    }
}
