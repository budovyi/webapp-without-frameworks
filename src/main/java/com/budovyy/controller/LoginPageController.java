package com.budovyy.controller;

import com.budovyy.web.Request;
import com.budovyy.web.ViewModel;

public class LoginPageController implements Controller {

    @Override
    public ViewModel process(Request req) {
        return ViewModel.of("login");
    }
}
