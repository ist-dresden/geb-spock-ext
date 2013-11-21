package test.framework.config.login

import geb.spock.GebSpec

class Login {

    public static Map<String, Closure> presets = [
            'CQ5.2': { spec, username, password ->
                LoginCQAuthor52 page = spec.to LoginCQAuthor52
                page.login(username, password)
            },
            'CQ5.6': { spec, username, password ->
                LoginCQAuthor56 page = spec.to LoginCQAuthor56
                page.login(username, password)
            }
    ]

    private String _username
    private String _password
    private String _strategy

    public void login(GebSpec spec) {
        Closure closure = presets.get(strategy)
        if (closure) {
            closure.call(spec, username, password)
        }
    }

    public String getUsername() {
        if (!_username) {
            getCredentials()
        }
        _username
    }

    public String getPassword() {
        if (!_password) {
            getCredentials()
        }
        _password
    }

    public String getStrategy() {
        if (!_strategy) {
            getCredentials()
            if (!_strategy) {
                _strategy = "CQ5.2"
            }
        }
        _strategy
    }

    /**
     * Parses the credentials from the 'credentials' system property
     * using the pattern {username}:{password}[@{login-rule}]
     * e.g. 'admin:admin@CQ5.6' or 'admin:admin' (default or target rule)
     */
    public void getCredentials() {
        String configuration = System.getProperty("credentials", "admin:admin")
        def matcher = (configuration =~ /([^\:]+)\:([^@]+)(@(.+))?/)
        if (matcher.matches()) {
            _username = matcher[0][1]
            _password = matcher[0][2]
            if (matcher.groupCount() == 4) {
                _strategy = matcher[0][4]
            }
        }
    }
}
