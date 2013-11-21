package test.framework.config.login

import geb.Page

class LoginCQAuthor56 extends Page {

    static url = 'libs/granite/core/content/login.html'

    static content = {
        usernameField { $("input", name: "j_username") }
        passwordField { $("input", name: "j_password") }
        loginButton { $("button") }
    }

    void login(String username, String password) {
        usernameField.value username
        passwordField.value password
        loginButton.click()
    }
}
