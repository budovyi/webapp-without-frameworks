package com.budovyy.dao;

import com.budovyy.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao extends AbstractDao<Product, Long>{

    public ProductDao(Connection connection) {
        super(connection);
    }

     public List<Product> getProductsByCategoryId(Long id) {
         String query = "SELECT * FROM products where fk_category_id=" + id;
         List<Product> list = new ArrayList<>();
         try {
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();
             while (resultSet.next()) {
                 Product product = new Product();
                 product.setId(resultSet.getLong("p_id"));
                 product.setProductName(resultSet.getString("p_productName"));
                 product.setDescription(resultSet.getString("p_description"));
                 product.setPrice(resultSet.getLong("p_price"));
                 list.add(product);
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }
         return list;
     }
}
