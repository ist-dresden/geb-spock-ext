package test.framework.geb

import geb.BuildAdapter
import geb.Configuration
import test.framework.SpecSuite
import test.framework.config.Target

class ExtendedConfiguration extends Configuration {

    static Target _target

    ExtendedConfiguration(ConfigObject rawConfig = null, Properties properties = null,
                          BuildAdapter buildAdapter = null, ClassLoader classLoader = null) {
        super(rawConfig, properties, buildAdapter, classLoader)
    }

    boolean getVerbose() {
        readValue('verbose', false)
    }

    String getBaseUrl() {
        String baseUrl = super.getBaseUrl()
        if (!baseUrl) {
            baseUrl = target.getBaseUrl()
        }
        baseUrl
    }

    Target getTarget() {
        if (!_target) {
            if (targetConf) {
                _target = targetConf.call()
            }
        }
        _target
    }

    String[] getSuiteClassNames() {
        String[] classNames = null
        if (suiteConf) {
            classNames = suiteConf.call()
        }
        if (!classNames || classNames.length < 1) {
            classNames = [System.getProperty("class", "TestSuite")]
        }
        classNames
    }

    Closure getSuiteConf() {
        readValue('suite', null)
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
