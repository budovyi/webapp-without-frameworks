package com.budovyy.service;

import com.budovyy.DBEmulator;
import com.budovyy.dao.CategoryDao;
import com.budovyy.model.Category;
import java.util.List;
import java.util.Optional;


public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;
    private static Long ident;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> getAll() {
        return categoryDao.getAll();
        //return null;
    }

    @Override
    public Optional<Category> getById(Long id) {
        ident = id;
        return Optional.ofNullable(categoryDao.getCategoryById(id));
    }

    public static Long getId() {
        return ident;
    }
}
