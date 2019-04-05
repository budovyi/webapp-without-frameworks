package com.budovyy.web;

import com.budovyy.Factory;
import com.budovyy.model.Role;
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

        userService = Factory.getUserServiceImpl(Factory.getUserDao(Factory.getConnection()));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Cookie[] cookies = req.getCookies();


        // logout
        if (req.getRequestURI().equals("/servlet/logout")) {
            Cookie temp = new Cookie("Mate_Application", null);
            temp.setMaxAge(0);
            resp.addCookie(temp);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // access with cookies
        if (openUri.contains(req.getRequestURI())) {
            processRequest(request, response, chain);
        } else  {
            Optional<User> user = Stream.of(cookies)
                    .filter(c -> c.getName().equals("Mate_Application"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .flatMap(userService::findByToken);

            boolean isAuthorized =  user.map(u -> u.getRoles().stream()
                    .anyMatch(r -> r.getRoleName().equals(Role.RoleName.USER)))
                    .orElse(false);

            if (isAuthorized) {
                processRequest(request, response, chain);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/notAllowed.jsp").forward(request, response);
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









/*

 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Cookie[] cookies = req.getCookies();


        // logout
        if (req.getRequestURI().equals("/servlet/logout")) {
            Cookie temp = new Cookie("Mate_Application", null);
            temp.setMaxAge(0);
            resp.addCookie(temp);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // access with cookies
        if (openUri.contains(req.getRequestURI())) {
            processRequest(request, response, chain);
        } else  {
            Optional<User> user = Stream.of(cookies)
                    .filter(c -> c.getName().equals("Mate_Application"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .flatMap(userService::findByToken);

            if (req.getRequestURI().startsWith("/servlet/admin")) {
                boolean isAuthorized =  user.map(u -> u.getRoles().stream()
                        .anyMatch(r -> r.getRoleName().equals(Role.RoleName.USER)))
                        .orElse(false);

                if (isAuthorized) {
                    processRequest(request, response, chain);
                } else {
                    dispatch(request, response, "notAllowed");
                }

            } else {
                if (user.isPresent()) {
                    processRequest(request, response, chain);
                } else {
                    dispatch(request, response, "login");
                }
            }
        }
    }




       @Override
    public void destroy() {
    }

    private void dispatch(ServletRequest request, ServletResponse response, String viewName) throws ServletException, IOException {
        request.getRequestDispatcher(String.format("/WEB-INF/views/%s.jsp", viewName)).forward(request, response);
    }

    private void processRequest(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}

*/