package test.framework.geb

import geb.Browser
import geb.Configuration
import geb.Page
import test.framework.config.Target

/**
 * Created by rwu on 11.02.14.
 */
class ExtendedBrowser extends Browser {

    private Boolean _loggedIn
    private String _reportGroup = null

    /**
     * Create a new browser backed by the given configuration.
     *
     * @see geb.Configuration
     */
    ExtendedBrowser(Configuration config) {
        super(config)
    }

    // Target and baseUrl

    Target getTarget() {
        config.target
    }

    // Extensions

    String getReportGroup() {
        _reportGroup
    }

    @Override
    void reportGroup(String path) {
        super.reportGroup(_reportGroup = path)
    }

    // extentions for browser navigation

    /**
     * Sends the browser to the given url.
     * If it is relative it is resolved against the {@link #getBaseUrl() base url}.
     * The extension performs an implicit login if the target requires that and
     * add implicit parameters configured for the target used currently
     */
    @Override
    void go(Map params, String url) {
        go(params, url, null)
    }

    /**
     * Sends the browser to the given url with an optional anchor inside of the page.
     * If it is relative it is resolved against the {@link #getBaseUrl() base url}.
     * This extension also performs an implicit login if the target requires that and
     * add implicit parameters configured for the target used currently
     */
    void go(Map params, String url, String anchor) {
        login()
        params.putAll(target.implicitParameters)
        String newUrl = calculateUri(url, params, anchor)
        if (driver.currentUrl == newUrl) {
            driver.navigate().refresh()
        } else {
            driver.get(newUrl)
        }
        if (!page) {
            page(Page)
        }
    }

    /**
     * reload the current page (useful to 'reinject' implicit parameters after browser actions)
     */
    void reload() {
        String url = driver.getCurrentUrl()
        String anchor = null
        int anchorStart = url.indexOf('#')
        if (anchorStart > 0) {
            anchor = url.substring(anchorStart)
            url = url.substring(0, anchorStart)
        }
        go([:], url, anchor)
    }

    // Login

    boolean getLoggedIn() {
        _loggedIn
    }

    boolean login() {
        if (_loggedIn == null) {
            _loggedIn = false
            if (config.loginConf) {
                if (config.verbose) {
                    println "login ${config.username}"
                }
                String currentBase = getBaseUrl()
                Page currentPage = getPage()
                setBaseUrl(target.serverUrl)
                config.loginConf.call(this, config.username, config.password)
                page(currentPage)
                setBaseUrl(currentBase)
                _loggedIn = true
            }
        }
        _loggedIn
    }

    void reset() {
        _loggedIn = null
        clearCookiesQuietly()
    }

    // extensions for the fucking 'privates'

    String calculateUri(String path, Map params, String anchor) {
        String uri = calculateUri(path, params)
        if (anchor) {
            if (!anchor.startsWith('#')) {
                uri += '#'
            }
            uri += anchor
        }
        uri
    }

    // fucking 'privates' (copied from superclass because they are not usable in extensions)

    protected String getBaseUrlRequired() {
        def baseUrl = getBaseUrl()
        if (baseUrl == null) {
            throw new geb.error.NoBaseUrlDefinedException()
        }
        baseUrl
    }

    protected String calculateUri(String path, Map params) {
        def uri
        if (path) {
            uri = new URI(path)
            if (!uri.absolute) {
                uri = new URI(getBaseUrlRequired()).resolve(uri)
            }
        } else {
            uri = new URI(getBaseUrlRequired())
        }

        def queryString = toQueryString(params)
        if (queryString) {
            def joiner = uri.query ? '&' : '?'
            new URL(uri.toString() + joiner + queryString).toString()
        } else {
            uri.toString()
        }
    }

    protected String toQueryString(Map params) {
        if (params) {
            params.collect { name, value ->
                def values = value instanceof Collection ? value : [value]
                values.collect { v ->
                    "${URLEncoder.encode(name.toString(), "UTF-8")}=${URLEncoder.encode(v.toString(), "UTF-8")}"
                }
            }.flatten().join("&")
        } else {
            ""
        }
    }
}
