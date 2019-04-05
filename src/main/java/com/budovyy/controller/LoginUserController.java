package com.budovyy.controller;

import com.budovyy.model.User;
import com.budovyy.web.Cookie;
import com.budovyy.web.Request;
import com.budovyy.web.ViewModel;
import com.budovyy.service.UserService;

public class LoginUserController implements Controller {

    private final UserService userService;

    public LoginUserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ViewModel process(Request req) {
        String username = req.getParam("username")[0];
        String password = req.getParam("password")[0];
        User user = User.of(username, password);


        return userService.authorize(user)
                .map(u -> ViewModel.of("welcome")
                        .withAttribute("user", u)
                        .withCookie(Cookie.of("Mate_Application", u.getToken())))
                .orElseGet(() -> ViewModel.of("login").withAttribute("error", "msg"));

    }
}
