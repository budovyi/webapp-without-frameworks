package com.budovyy.controller;

import com.budovyy.web.Request;
import com.budovyy.web.ViewModel;
import com.budovyy.service.CategoryService;

public class GetAllCategoriesController implements Controller {

    private final CategoryService categoryService;

    public GetAllCategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public ViewModel process(Request req) {
        return ViewModel.of("categories").withAttribute("categories",categoryService.getAll());
    }
}
