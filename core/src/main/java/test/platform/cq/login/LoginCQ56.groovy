package test.platform.cq.login

import geb.Page

class LoginCQ56 extends Page {

    static url = 'libs/granite/core/content/login.html'

    void login(String username, String password) {
        $("input", name: "j_username").value username
        $("input", name: "j_password").value password
        $("button").click()
    }
}
