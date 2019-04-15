package com.budovyy;

import com.budovyy.controller.*;
import com.budovyy.dao.CategoryDao;
import com.budovyy.dao.ProductDao;
import com.budovyy.dao.UserDao;
import com.budovyy.dao.UserDaoImpl;
import com.budovyy.service.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Factory {

    private static Connection connection;

    static {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test-db3",
                    "sa", "");
        } catch (ClassNotFoundException  |SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    // delete if use lambda in MainServlet
    public static LoginPageController getLoginPageController() {
        return new LoginPageController();
    }

    public static LoginUserController getLoginUserController(UserService userService) {
        return new LoginUserController(userService);
    }

    public static GetAllCategoriesController getAllCategoriesController(CategoryService categoryService) {
        return new GetAllCategoriesController(categoryService);
    }


    public static UserService getUserServiceImpl(UserDao userDao) {
        return new UserServiceImpl(userDao);
    }

    public static CategoryService getCategoryService (CategoryDao categoryDao) {
        return new CategoryServiceImpl(categoryDao);
    }

    public static GetCategoryByIdController getCategoryByIdController(CategoryService categoryService) {
        return new GetCategoryByIdController(categoryService);

    }

    public static GetProductController getProductController (ProductService productService) {
       return new GetProductController(productService);
    }

    public static ProductService getProductService(ProductDao productDao) {
        return new ProductServiceImpl(productDao);
    }

    public static UserDao getUserDao(Connection connection) {
        return new UserDaoImpl(connection);
    }

    public static CategoryDao getCategoryDao(Connection connection) {
        return new CategoryDao(connection);
    }

    public static ProductDao getProductDao(Connection connection) {
        return new ProductDao(connection);
    }
}
