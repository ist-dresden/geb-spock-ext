import test.framework.geb.AdaptedNavigatorFactory

Boolean _verbose = Boolean.parseBoolean(System.getProperty('verbose', 'true'))
this.verbose = _verbose

navigatorFactory = { browser ->
    new AdaptedNavigatorFactory(browser)
}

loadConfig = { configClassName, configOption, defaultOptionValue = null ->
    try {
        Class configClass = Class.forName(configClassName)
        ConfigObject configObject = new ConfigSlurper(
                System.getProperty(configOption, defaultOptionValue)).parse(configClass)
        if (this.config) {
            this.config = this.config.merge(configObject)
        } else {
            this.config = configObject
        }
        if (_verbose) {
            println "configuration extended for aspect '${configOption}' (${configClassName})"
        }
    } catch (ClassNotFoundException cnfex) {
        if (_verbose) {
            println "no configuration loaded for aspect '${configOption}' (${configClassName})"
        }
    }
}

loadConfig.call('ReportConfig', 'report')
loadConfig.call('DriverConfig', 'driver')
loadConfig.call('DefaultTargetConfig', 'target')
loadConfig.call('TargetConfig', 'target')

def target = this.config.target?.call()
def loginRule = target.loginRule
/**
 * Parses the credentials from the 'credentials' system property
 * using the pattern {username}:{password}[@{login-rule}]
 * e.g. 'admin:admin@CQ5.6' or 'admin:admin' (default or target rule)
 */
String credentialsProperty = System.getProperty('credentials', 'admin:admin')
def matcher = (credentialsProperty =~ /([^\:]+)\:([^@]+)(@(.+))?/)
if (matcher.matches()) {
    this.username = matcher[0][1]
    this.password = matcher[0][2]
    if (matcher.groupCount() == 4 && matcher[0][4]) {
        loginRule = matcher[0][4]
    }
}

loadConfig.call('BuiltinLoginConfig', 'login', loginRule)
loadConfig.call('LoginConfig', 'login', loginRule)
