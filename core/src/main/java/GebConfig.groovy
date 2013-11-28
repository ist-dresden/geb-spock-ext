import test.framework.geb.AdaptedNavigatorFactory

Boolean _verbose = Boolean.parseBoolean(System.getProperty('verbose', 'true'))
this.verbose = _verbose

// ---- Geb framework initialization (extension) ----

navigatorFactory = { browser ->
    new AdaptedNavigatorFactory(browser)
}

// ---- configuration extension (config modularization) ----

loadConfig = { configClassName, configOption, defaultOptionValue = null ->
    String configKey = System.getProperty(configOption, defaultOptionValue)
    try {
        Class configClass = Class.forName(configClassName)
        ConfigObject configObject = new ConfigSlurper(configKey).parse(configClass)
        if (this.config) {
            this.config = this.config.merge(configObject)
        } else {
            this.config = configObject
        }
        if (_verbose) {
            println "configuration extended for aspect '${configOption}':'${configKey}' (${configClassName})"
        }
    } catch (ClassNotFoundException cnfex) {
        if (_verbose) {
            println "no configuration loaded for aspect '${configOption}':'${configKey}' (${configClassName})"
        }
    }
}

// ---- Report configuration ----

/** framework default report configuration */
loadConfig.call('DefaultReportConfig', 'report')
/** overlay default report configuration with the optional project configuration */
loadConfig.call('ReportConfig', 'report')

// ---- Driver configuration ----

/** normally necessary project driver configuration */
loadConfig.call('DriverConfig', 'driver')

// ---- Target configuration ----

/** framework default target configuration (for local tests only) */
loadConfig.call('DefaultTargetConfig', 'target')
/** overlay target configuration with the normally necessary project configuration */
loadConfig.call('TargetConfig', 'target')

// ---- Login configuration ----

/**
 * determine login rule (policy key) by the configured target
 */
def _target = this.config.target?.call()
def _loginRule = _target?.loginRule
/**
 * Parse the credentials from the 'credentials' system property
 * using the pattern {username}:{password}[@{login-rule}]
 * e.g. 'admin:admin@CQ56' or 'admin:admin' (default or target login rule)
 */
String _credentialsProperty = System.getProperty('credentials', 'admin:admin')
def _matcher = (_credentialsProperty =~ /([^\:]+)\:([^@]+)(@(.+))?/)
if (_matcher.matches()) {
    this.username = _matcher[0][1]
    this.password = _matcher[0][2]
    if (_matcher.groupCount() == 4 && _matcher[0][4]) {
        _loginRule = _matcher[0][4]
    }
}

/** framework builtin login configuration */
loadConfig.call('BuiltinLoginConfig', 'login', _loginRule)
/** overlay builtin login configuration with the optional project configuration */
loadConfig.call('LoginConfig', 'login', _loginRule)

/** project test suite configuration */
loadConfig.call('SuiteConfig', 'suite')
