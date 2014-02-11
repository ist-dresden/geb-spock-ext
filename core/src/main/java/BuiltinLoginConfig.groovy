import test.platform.cq.login.LoginCQ52
import test.platform.cq.login.LoginCQ56

environments {

    CQ52 {
        login = { spec, username, password ->
            LoginCQ52 page = spec.to LoginCQ52
            page.login(username, password)
        }
    }

    CQ53 {
        login = { spec, username, password ->
            LoginCQ52 page = spec.to LoginCQ52
            page.login(username, password)
        }
    }

    CQ56 {
        login = { spec, username, password ->
            LoginCQ56 page = spec.to LoginCQ56
            page.login(username, password)
        }
    }
}
