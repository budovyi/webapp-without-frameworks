package com.budovyy.service;

import com.budovyy.model.Product;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProduct(String productName);
}
