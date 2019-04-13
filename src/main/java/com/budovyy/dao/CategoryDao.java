package com.budovyy.dao;

import com.budovyy.Factory;
import com.budovyy.model.Category;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.List;

public class CategoryDao extends AbstractDao<Category, Long> {

    public CategoryDao(Connection connection) {
        super(connection);
    }


    public static void main(String[] args) {
        CategoryDao categoryDao = new CategoryDao(Factory.getConnection());

        /*Category  c = categoryDao.create(new Category(-1L, "Bubbles", "WE DID IT!)"));
        System.out.println(c);
*/
/*
        Category c1 = categoryDao.getById(3L);
        System.out.println(c1);
*/

/*
        List<Category> listCategories = categoryDao.getAll();
        System.out.println(listCategories);
*/

        /*categoryDao.delete(1L);*/

        Category category = new Category(3L, "TestCategory", "Desc");
        Category c = categoryDao.update(category);
        System.out.println(c);

    }
}







