package com.budovyy.web;

import com.budovyy.Factory;
import com.budovyy.model.Role;
import com.budovyy.model.User;
import com.budovyy.service.UserService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class AdminFilter implements Filter {

    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = Factory.getUserServiceImpl(Factory.getUserDao(Factory.getConnection()));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Cookie[] cookies = req.getCookies();


        if (req.getRequestURI().startsWith("/servlet/admin")) {

            Optional<User> user = Stream.of(cookies)
                    .filter(c -> c.getName().equals("Mate_Application"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .flatMap(userService::findByToken);


            boolean isAuthorized =  user.map(u -> u.getRoles().stream()
                    .anyMatch(r -> r.getRoleName().equals(Role.RoleName.ADMIN)))
                    .orElse(false);

            if (isAuthorized) {
                chain.doFilter(req, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/notAllowed.jsp").forward(request, response);
            }
        } else {
            chain.doFilter(req, response);
        }
    }

    @Override
    public void destroy() {

    }
}
