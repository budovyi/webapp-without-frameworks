package com.budovyy.web;

import com.budovyy.controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.budovyy.Factory.*;


public class MainServlet extends HttpServlet {

    private static final Map<Request, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put(Request.of("GET", "/servlet/login"), getLoginPageController()); // , r -> ViewModel.of("login");
        controllerMap.put(Request.of("POST", "/servlet/login"), getLoginUserController(getUserServiceImpl(getUserDao(getConnection()))));
        controllerMap.put(Request.of("GET", "/servlet/categories"), getAllCategoriesController(getCategoryService(getCategoryDao(getConnection()))));
        controllerMap.put(Request.of("GET", "/servlet/category"), getCategoryByIdController(getCategoryService(getCategoryDao(getConnection()))));
        controllerMap.put(Request.of("GET","/servlet/product"), getProductController(getProductService()));
        controllerMap.put(Request.of("GET", "/servlet/admin"), r -> ViewModel.of("admin"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Request request = Request.of(req.getMethod(), req.getRequestURI(), req.getParameterMap());
        Controller controller = controllerMap.getOrDefault(request, r -> ViewModel.of("404"));
        ViewModel vm = controller.process(request);
        processViewModel(vm, req, resp);
    }

    private void processViewModel(ViewModel vm, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //vm.getAttributes().forEach((k, v) -> req.setAttribute(k, v));
        vm.getAttributes().forEach(req::setAttribute);
        processCookies(vm.getCookies(), resp);

        req.getRequestDispatcher(vm.getRedirectUri()).forward(req, resp);
    }

    private void processCookies(List<Cookie> cookie, HttpServletResponse response) {
        cookie.forEach(c -> response.addCookie(new javax.servlet.http.Cookie(c.getName(), c.getValue())));
    }
}
