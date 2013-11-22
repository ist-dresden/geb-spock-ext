package test.framework.config

class Target {

    private String _serverUrl
    private String _basePath
    private String _loginRule

    public Target(String serverUrl,
                  String basePath,
                  String loginPule) {
        _serverUrl = serverUrl
        _basePath = basePath
        _loginRule = loginPule
        if (!_serverUrl.endsWith('/')) {
            _serverUrl += '/'
        }
        if (!_basePath.endsWith('/')) {
            _basePath += '/'
        }
        while (_basePath.startsWith('/')) {
            _basePath = _basePath.substring(1)
        }
    }

    public String getBaseUrl(String path = basePath) {
        if (!path.endsWith('/')) {
            path += '/'
        }
        while (path.startsWith('/')) {
            path = path.substring(1)
        }
        serverUrl + path
    }

    public String getServerUrl() {
        _serverUrl
    }

    public String getBasePath() {
        _basePath
    }

    public String getLoginRule() {
        _loginRule
    }
}
