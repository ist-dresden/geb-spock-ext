import test.framework.config.Target

localPort = System.getProperty("localPort", "4502")

target = {
    new Target("http://localhost:${localPort}/", 'content/geometrixx', 'CQ56')
}

environments {

    back_to_local_cq {
        target = {
            new Target("http://172.16.218.1:${localPort}/", 'content/geometrixx', 'CQ56')
        }
    }

    google_live {
        target = {
            new Target("http://www.google.com/", '', '')
        }
    }
}