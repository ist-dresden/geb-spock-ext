import test.framework.config.Target

localPort = System.getProperty("localPort", "8080")

target = {
    new Target("http://localhost:${localPort}/", '', '')
}
