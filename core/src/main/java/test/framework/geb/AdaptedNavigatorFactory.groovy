package test.framework.geb

import geb.navigator.factory.BrowserBackedNavigatorFactory

class AdaptedNavigatorFactory extends BrowserBackedNavigatorFactory {

    AdaptedNavigatorFactory(geb.Browser browser) {
        super(browser, new AdaptedInnerNavigatorFactory())
    }
}
