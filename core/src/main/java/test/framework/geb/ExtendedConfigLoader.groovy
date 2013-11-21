package test.framework.geb

import geb.ConfigurationLoader

class ExtendedConfigLoader extends ConfigurationLoader {

    ExtendedConfigLoader(String environment, Properties properties, GroovyClassLoader classLoader) {
        super(environment, properties, classLoader)
    }

    protected createConf(ConfigObject rawConfig, GroovyClassLoader classLoader) {
        ConfigObject extensions = rawConfig.get('config')
        if (extensions) {
            rawConfig.merge(extensions)
            rawConfig.remove('config')
        }
        new ExtendedConfiguration(rawConfig, properties, createBuildAdapter(classLoader), classLoader)
    }
}
