package test.platform.cq.login

import geb.Page

class LoginCQ52 extends Page {

    static url = '/libs/cq/core/content/login.html'

    void login(String username, String password) {
        $("#usr input").value username
        $("#pwd input").value password
        $("#submit input").click()
    }
}
