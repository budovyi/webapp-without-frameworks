package com.budovyy.service;

import com.budovyy.model.Category;
import com.budovyy.model.Product;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getAll();

    Optional<Category> getById(Long id);

}
