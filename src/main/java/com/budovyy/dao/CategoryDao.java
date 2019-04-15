package com.budovyy.dao;

import com.budovyy.Factory;
import com.budovyy.model.Category;
import com.budovyy.model.Product;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.budovyy.Factory.*;

public class CategoryDao extends AbstractDao<Category, Long> {

    public CategoryDao(Connection connection) {
        super(connection);
    }

    public Category getCategoryById(Long id) {
        Category category = getById(id);
        List<Product> products = getProductDao(connection).getProductsByCategoryId(id);

        category.setProducts(products);
        return category;
    }




   /* public static void main(String[] args) {
        CategoryDao categoryDao = new CategoryDao(Factory.getConnection());
*/
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

     /*   Category category = new Category(4L, "TestCategory", "Desc");
        Category c = categoryDao.update(category);
        System.out.println(c);*/

    //}
}







