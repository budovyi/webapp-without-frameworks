package com.budovyy.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T, ID> implements GenericDao<T, ID> {

    private Connection connection;

    private Class clazz = getGenericClass();
    private String fullClassName = clazz.getName();
    private String clearClassName = getClearClassName(fullClassName);
    private char symbol = clearClassName.charAt(0);

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public T create(T t) {
        StringBuilder sb = new StringBuilder();

        // create table
        PreparedStatement statement;
        String query = getQueryForCreateTable();
        try {
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // adding  T  to created table
        sb.append("INSERT INTO ").append(clearClassName).append("S").append(" VALUES( ");

        Class c = t.getClass();
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            String value = null;
            if (field.getName().toUpperCase().equals("ID")) {
                value = null;
                sb.append(value).append(", ");
            } else {
                String fieldType = field.getAnnotatedType().getType().getTypeName()
                        .replaceFirst("java.lang.", "");
                if (!fieldType.equalsIgnoreCase("List")) {
                    try {
                        value = String.valueOf(field.get(t));
                        sb.append(" '").append(value).append("', ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        sb.append(")");






        // return T with set ID


        Statement st;
        String insertQuery = sb.toString();
        ResultSet rs;
        try {
            st = connection.createStatement();
            st.executeUpdate(insertQuery);
           // rs = st.executeQuery(insertQuery);


            String selectQuery = "SELECT * FROM CATEGORYS WHERE C_ID='5'";  // temporary
            rs = st.executeQuery(selectQuery);          // NEED TO CREATE SELECT QUERY FOR DB !!!!!!!!!!!!!

            Long id = null;
            System.out.println(rs);
            if (rs.next()) {
              id  = rs.getLong("C_ID");
            }
            System.out.println(id);

            Field field = c.getDeclaredField("id");
            field.setAccessible(true);
            field.set(t, rs.getLong("C_ID")); // temporary C_ID, should change to flexible variant


        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(t);
        return t;
    }

    @Override  //TODO        some troubles in parseResultSet
    public T getById(ID id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(clearClassName).append("S")
                .append(" WHERE ").append(symbol).append("_").append("ID='").append(id).append("'");
        PreparedStatement statement;
        ResultSet resultSet;
        T t = null;
        try {
            statement = connection.prepareStatement(sb.toString());
            resultSet = statement.executeQuery();
            t = parseResultSet(resultSet).get(0);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public List<T> getAll() {
        List<T> list = null;
        String query =  "SELECT * FROM " + clearClassName + "S";
        try  {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public T update(T t) {
        return null;
    }

    @Override
    public void delete(ID id) {
    }


    // TODO         save Long or int like a String to DB
    private String getQueryForCreateTable() {
        StringBuilder sb = new StringBuilder();
        String fieldType;
        sb.append( " IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES  WHERE TABLE_NAME = N'").append(clearClassName).append("S").append("')   BEGIN END  ELSE    BEGIN  CREATE TABLE ")
                .append(clearClassName).append("S").append(" ( ");

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fieldType = field.getAnnotatedType().getType().getTypeName()
                    .replaceFirst("java.lang.", "").toUpperCase();

            if (field.getName().toUpperCase().equals("ID") ||
                    fieldType.equals("INT") ||
                    fieldType.equals("LONG")) {
                sb.append(symbol).append("_").append("ID BIGINT PRIMARY KEY AUTO_INCREMENT, ");
            } else {
                sb.append(symbol).append("_").append(field.getName()).append(" VARCHAR(255) NOT NULL, ");
            }
        }
        sb.append(" )    End  ");
        return sb.toString();
    }

    // TODO     change rs.getLong to rs.getString coz we have ID id, not Long id
    // TODO     OR  !!!! change rs.getLong to check a type of field and the set Long or String
    // TODO     IF util.List ->  should create another Obj-s and set a values
    private List<T> parseResultSet(ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException   {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            T clz = (T)  getGenericClass().newInstance();
            Class<?> c = clz.getClass();  //   Class<?> c ???

            Field field;
            field = c.getDeclaredFields()[0];
            field.setAccessible(true);
            field.set(clz, rs.getLong("C_ID"));      // temporary

            for (int i = 1; c.getDeclaredFields().length > i; i++) {
                field = c.getDeclaredFields()[i];
                field.setAccessible(true);
                field.set(clz, rs.getString(i + 1));
            }
            list.add(clz);
        }
        return list;
    }

    private String getClearClassName(String fullClassName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fullClassName.length(); i++) {
            sb.append(fullClassName.charAt(i));

            if (fullClassName.charAt(i) == ('.')) {
                sb.setLength(0);
            }
        }
        return sb.toString();
    }

    public Class getGenericClass() {
        Class<T> persistentClass = (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        return persistentClass;
    }
}
