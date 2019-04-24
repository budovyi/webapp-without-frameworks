package com.budovyy.dao;

import com.budovyy.model.Category;
import com.budovyy.model.Product;
import org.h2.command.Prepared;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl   {

    private final Connection connection;

    public CategoryDaoImpl(Connection connection) {
        this.connection = connection;
    }






    public Category getCategoryById(Long id) {
        String query = "SELECT C.ID AS C_ID, " +
                "C.CATEGORY_NAME AS C_NAME, " +
                "C.CATEGORY_DESCRIPTION AS C_DESC, " +
                "P.ID AS P_ID, " +
                "P.PRODUCTS_NAME AS P_NAME, " +
                "P.PRODUCTS_DESCRIPTION AS P_DESC, " +
                "P.PRICE AS P_PRICE FROM CATEGORIES C " +
                "JOIN PRODUCTS P ON C.ID = P.FK_CATEGORY_ID " +
                "WHERE C.ID = ?";

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Category result = null;
        try {
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            result = getCategoryWithProductsFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Category getCategoryWithProductsFromResultSet(ResultSet rs) throws SQLException {
        List<Product> products = new ArrayList<>();
        Category result = null;

        if (rs.next()) {
            result = getCategoryFromResultSet(rs);

            while (!rs.isAfterLast()) {
                products.add(getProductFromResultSet(rs));
                rs.next();
            }
            result.setProducts(products);
        }
        return result;
    }

    private Category getCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("C_ID");
        String categoryName = resultSet.getString("C_NAME");
        String categoryDesc = resultSet.getString("C_DESC");
        return new Category(id, categoryName, categoryDesc);
    }

    private Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("P_ID");
        String productName = resultSet.getString("P_NAME");
        String productDesc = resultSet.getString("P_DESC");
        double price = resultSet.getDouble("P_PRICE");
        return new Product(id, productName, productDesc, price);
    }
}
