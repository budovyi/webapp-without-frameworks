package com.budovyy.service;

import com.budovyy.DBEmulator;
import com.budovyy.model.Category;
import com.budovyy.model.Product;
import java.util.Optional;

public class ProductServiceImpl implements ProductService{

    @Override
    public Optional<Product> getProduct(String productName) {

        // shoud replace categories to DB
        Category category = DBEmulator.getCategories().stream()
                .filter(c -> c.getId().equals(CategoryServiceImpl.getId()))
                .findFirst().get();

        return category.getProducts().stream().filter(p -> p.getProductName().equals(productName)).findFirst();
    }
}


