package com.budovyy.service;

import com.budovyy.DBEmulator;
import com.budovyy.dao.ProductDao;
import com.budovyy.model.Category;
import com.budovyy.model.Product;

import java.util.Optional;

public class ProductServiceImpl implements ProductService{

    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao){
        this.productDao = productDao;
    }


    @Override
    public Optional<Product> getProduct(Long id) {


        return Optional.of(productDao.getById(id));

/*
        Category category1 = DBEmulator.getCategories().stream()
                .filter(c -> c.getId().equals(CategoryServiceImpl.getId()))
                .findFirst().get();

        return category1.getProducts().stream().filter(p -> p.getProductName().equals(productName)).findFirst();*/
    }
}


