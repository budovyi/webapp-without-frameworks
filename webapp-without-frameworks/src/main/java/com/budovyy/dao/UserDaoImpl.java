package com.budovyy.dao;

import com.budovyy.Factory;
import com.budovyy.model.User;

import java.sql.*;

public class UserDaoImpl implements UserDao {

    private final Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public User addUser(User user) {
//        String query = "INSERT INTO USERS VALUES("
//                + user.getId() + ", '"
//                + user.getUsername()+ "', '"
//                + user.getPassword() + "', '"
//                + user.getToken() + "', '"
//                + user.getFirstName() + "', '"
//                + user.getLastName() + "')";


        String  query  = "INSERT INTO USERS VALUES(?,?,?,?,?,?)";

        PreparedStatement statement;
        try {
            statement  =  connection.prepareStatement(query);
            statement.setNull(1,-5);
            statement.setString(2,user.getUsername());
            statement.setString(3,user.getPassword());
            statement.setString(4,user.getToken());
            statement.setString(5,user.getFirstName());
            statement.setString(6,user.getLastName());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                user.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        User user =  new User(null,
                "admin2",
                "123",
                "1111",
                "Anton",
                "LastName");

        UserDao userDao = new UserDaoImpl(Factory.getConnection());
        User result = userDao.addUser(user);
        System.out.println(result);
        System.out.println(result.getId());
    }
}
