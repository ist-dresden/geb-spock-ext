import test.framework.config.Target

localPort = System.getProperty("localPort", "4502")

target = {
    new Target("http://localhost:${localPort}/", 'content/geometrixx', 'CQ56')
}

environments {

    google_live {
        target = {
            new Target("http://www.google.com/", '', '')
        }
    }
}