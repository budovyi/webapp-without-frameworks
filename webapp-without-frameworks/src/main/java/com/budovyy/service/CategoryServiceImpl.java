package com.budovyy.service;

import com.budovyy.DBEmulator;
import com.budovyy.model.Category;
import java.util.List;
import java.util.Optional;


public class CategoryServiceImpl implements CategoryService {

    private static Long ident;

    public static Long getId() {
        return ident;
    }

    @Override
    public List<Category> getAll() {
        return DBEmulator.getCategories();
    }

    @Override
    public Optional<Category> getById(Long id) {
        ident = id;
        return DBEmulator.getCategories().stream().filter(c -> c.getId().equals(id)).findFirst();
    }
}