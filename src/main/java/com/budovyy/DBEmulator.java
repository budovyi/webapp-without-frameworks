package com.budovyy;

import com.budovyy.model.Category;
import com.budovyy.model.Product;
import com.budovyy.model.User;

import java.util.ArrayList;
import java.util.List;

public class DBEmulator {

    private static List<Category> categories = new ArrayList<>();

    private static List<User> users = new ArrayList<>();

    static {
        Product product = new Product("Iphone", "apple phone", 990.90);
        Product product2 = new Product("samsung", "samsung phone", 690.90);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);


        Category category = new Category(1L, "Mobile Phones", "best phones");
        category.setProducts(products);


        categories.add(category);
        categories.add(new Category(2L, "Shoes", "Italian Shoes"));
        categories.add(new Category(3L, "TV", "BEST TV for best price"));

        users.add(new User(null,"admin",
                "96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1e", //123123
                "7f721680-c4ce-49c4-a309-e181d40cd848",
                "Anton",
                "LastName"));

    }

    public static List<User> getUsers() {
        return users;
    }


    public static void addUser(User user) {
        users.add(user);
    }

    public static List<Category> getCategories() {
        return categories;
    }
}

