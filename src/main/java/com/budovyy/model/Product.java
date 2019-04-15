package com.budovyy.model;

public class Product {
    private Long id;
    //private static Long categoryId;   // or field group
    private String productName;
    private String description;
    private double price;

    public Product () {
    }
    public Product(Long id, String productName, String description, double price) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.price = price;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //    public static Long getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(Long categoryId) {
//        this.categoryId = categoryId;
//    }
}
