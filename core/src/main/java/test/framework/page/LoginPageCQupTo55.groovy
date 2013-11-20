package test.framework.page

import geb.Page

class LoginPageCQupTo55 extends LoginPage {

    static url = 'libs/wcm/auth/content/login.html'

    static boolean loggedIn

    static content = {
        usernameField { $("#usr input") }
        passwordField { $("#pwd input") }
        loginButton { $("#submit input") }
    }

    Page login(username = 'admin', password = 'dhl123') {
        if (!loggedIn) {
            usernameField.value username
            passwordField.value password
            loginButton.click()
            loggedIn = true
        }
        this
    }
}
