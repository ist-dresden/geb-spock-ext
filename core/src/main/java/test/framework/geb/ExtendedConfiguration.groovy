package test.framework.geb

import geb.BuildAdapter
import geb.Configuration
import test.framework.SpecSuite

class ExtendedConfiguration extends Configuration {

    ExtendedConfiguration(ConfigObject rawConfig = null, Properties properties = null,
                          BuildAdapter buildAdapter = null, ClassLoader classLoader = null) {
        super(rawConfig, properties, buildAdapter, classLoader)
    }

    boolean getVerbose() {
        readValue('verbose', false)
    }

    String[] getSuiteClassNames() {
        Closure suiteConf = readValue('suite', null)
        String[] classNames = null
        if (suiteConf) {
            classNames = suiteConf.call()
        }
        if (!classNames || classNames.length < 1) {
            classNames = [System.getProperty("class", "TestSuite")]
        }
        classNames
    }

    Closure getReportConf() {
        readValue('report', null)
    }

    Closure getDriverConf() {
        readValue('driver', null)
    }

    Closure getTargetConf() {
        readValue('target', null)
    }

    Closure getLoginConf() {
        readValue('login', null)
    }

    String getUsername() {
        readValue('username', '')
    }

    String getPassword() {
        readValue('password', '')
    }
}
