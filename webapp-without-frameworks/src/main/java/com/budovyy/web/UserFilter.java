package com.budovyy.web;

import com.budovyy.Factory;
import com.budovyy.model.User;
import com.budovyy.service.UserService;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class UserFilter implements Filter {

    private Set<String> openUri = new HashSet<>();

    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        openUri.add("/servlet/login");
        openUri.add("/servlet/register");
        //openUri.add("/servlet/logout");
        userService = Factory.getUserServiceImpl();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Cookie[] cookies = req.getCookies();
        Cookie disc ;
        if (req.getRequestURI().equals("/servlet/logout")) {
            Cookie temp = new Cookie("Mate_Application", null);
            temp.setMaxAge(0);
            resp.addCookie(temp);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        if (openUri.contains(req.getRequestURI())) {
            processRequest(request, response, chain);
        } else {
            Optional<User> user = Stream.of(cookies)
                    .filter(c -> c.getName().equals("Mate_Application"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .flatMap(userService::findByToken);

            if (user.isPresent()) {
                processRequest(request, response, chain);
            } else {
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }
        }
    }

    @Override
    public void destroy() {

    }

    private void processRequest(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}