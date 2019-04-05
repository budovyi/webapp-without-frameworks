package com.budovyy.dao;

import com.budovyy.Factory;
import com.budovyy.model.Category;

import java.lang.reflect.Field;
import java.sql.Connection;

public class CategoryDao extends AbstractDao<Category, Long> {

    public CategoryDao(Connection connection) {
        super(connection);
    }


    public static void main(String[] args) {
        CategoryDao categoryDao = new CategoryDao(Factory.getConnection());

//        String result = categoryDao.getGenericClass().getName();
//        Class clazz = categoryDao.getGenericClass();
//        clazz.getDeclaredFields();
//
//        System.out.println(result);
//        for (Field field : clazz.getDeclaredFields()) {
//            System.out.println(field.getName());
//        }



        Category  c = categoryDao.create(new Category(-1L, "Bubbles", "WE DID IT!)"));
        System.out.println(c);
//        Category c1 = categoryDao.getById(7L);
//        System.out.println(c1);
    }
}







