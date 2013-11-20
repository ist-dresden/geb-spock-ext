package test.framework.page

import geb.Page

class LoginPage extends Page {

    static url = 'libs/granite/core/content/login.html'

    static content = {
        usernameField { $("input", name: "j_username") }
        passwordField { $("input", name: "j_password") }
        loginButton { $("button") }
    }

    Page login(username = 'admin', password = 'admin') {
        usernameField.value username
        passwordField.value password
        loginButton.click()
        this
    }
}
