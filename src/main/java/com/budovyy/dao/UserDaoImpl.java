package com.budovyy.dao;

import com.budovyy.Factory;
import com.budovyy.model.Role;
import com.budovyy.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private final Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User addUser(User user) {
        String  query  = "INSERT INTO USERS VALUES(?,?,?,?,?,?)";

        PreparedStatement statement;
        try {
            statement  =  connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setNull(1,-1);
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

    @Override
    public User getByToken(String token) {

        /*
        * select u.id as u_id,
                u.username,
                u.password,
                u.token,
                u.first_name,
                u.last_name,
                r.id as r_id,
                r.role_name
                from users u
                join users_to_roles utr on u.id = utr.fk_user_id
                join roles r on utr.fk_role_id = r.id
               where u.token = 'token4'
        *
        * */
        String query = "select u.id as u_id, " +
                "u.username," +
                "u.password, " +
                "u.token, " +
                "u.first_name, " +
                "u.last_name, " +
                "r.id as r_id, " +
                "r.role_name " +
                "from users u " +
                "join users_to_roles utr on u.id = utr.fk_user_id " +
                "join roles r on utr.fk_role_id = r.id " +
                "where u.token = ?";
        PreparedStatement statement;
        ResultSet resultSet = null;
        User result = null;
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, token);
            resultSet = statement.executeQuery();
            result = getUserWithRolesFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        String query = "SELECT U.ID AS U_ID, " +
                "U.USERNAME, " +
                "U.PASSWORD, " +
                "U.TOKEN, " +
                "U.FIRST_NAME, " +
                "U.LAST_NAME " +
                "FROM USERS U " +
                "WHERE U.USERNAME = ?";
        PreparedStatement statement;
        ResultSet resultSet = null;
        User result = null;
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(result);
    }

    private User getUserWithRolesFromResultSet(ResultSet resultSet) throws SQLException {
        List<Role> roles = new ArrayList<>();
        User result = null;

        if (resultSet.next()) {
            result = getUserFromResultSet(resultSet);
            result.setRoles(roles);   // ??

            while (!resultSet.isAfterLast()) {
                roles.add(getRoleFromResultSet(resultSet));
                resultSet.next();
            }
        }

        return result;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("u_id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String token = resultSet.getString("token");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        return new User(id, username, password, token, firstName, lastName);
    }

    private Role getRoleFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("r_id");
        String roleName = resultSet.getString("role_name");
        return new Role(id, Role.RoleName.valueOf(roleName));
    }


//    public static void main(String[] args) {
//        UserDao userDao = new UserDaoImpl(Factory.getConnection());
//        User user = userDao.getByToken("token4");
//        System.out.println(user);
//    }

//    public static void main(String[] args) {
//        CategoryDao categoryDao = new CategoryDaoImpl(Factory.getConnection());
//        Category category = categoryDao.getCategoryById(1L);
//        System.out.println(category);
//    }

//    public static void main(String[] args) {
//        User user =  new User(null,
//                "admin",
//                "123123",
//                "1111",
//                "Anton",
//                "LastName");
//
//        UserDao userDao = new UserDaoImpl(Factory.getConnection());
//        User result = userDao.addUser(user);
//        System.out.println(result);
//
//    }
}
