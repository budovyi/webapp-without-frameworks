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
    private boolean isList;


    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public T create(T t) {
        StringBuilder sb = new StringBuilder();

        // create table
        String query = getQueryForCreateTable();
        executeUpdate(query);

        // adding  T  to created table
        sb.append("INSERT INTO ").append(clearClassName).append("S").append(" VALUES( ");

        Class c = t.getClass();
        String value = null;
        String fieldType;
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            fieldType = field.getAnnotatedType().getType().getTypeName()
                    .replaceFirst("java.lang.", "");

            if (field.getName().toUpperCase().equals("ID")) {
                value = null;
                sb.append(value).append(", ");
            } else {
                isList = fieldType.matches("(.*)List(.*)");
                if (!isList) {
                    try {
                        value = String.valueOf(field.get(t));
                        sb.append(" '").append(value).append("', ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        sb.append(");");

        // return T with set ID
        Long id = null;
        String insertQuery = sb.toString();
        ResultSet resultSet = executeUpdate(insertQuery);
        try {
            if (resultSet.next()) {
                id = resultSet.getLong(1);
            }

            Field field = c.getDeclaredField("id");
            field.setAccessible(true);
            field.set(t, id);

        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public T getById(ID id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(clearClassName).append("S")
                .append(" WHERE ").append(symbol).append("_ID='").append(id).append("'");
        PreparedStatement statement = connection.prepareStatement();
        ResultSet resultSet = executeQuery(sb.toString());
        return parseResultSet(resultSet).get(0);
    }

    @Override
    public List<T> getAll() {
        String query = "SELECT * FROM " + clearClassName + "S";
        ResultSet rs = executeQuery(query);
        return parseResultSet(rs);
    }

    @Override
    public T update(T t) {
        String update = "UPDATE " + clearClassName + "S SET ";
        StringBuilder query = new StringBuilder();
        Class c = t.getClass();
        Long id = null;

        try {
            Field f = c.getDeclaredField("id");
            f.setAccessible(true);
            id = (Long) f.get(t);
            f.set(t, id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldType = field.getAnnotatedType().getType().getTypeName()
                    .replaceFirst("java.lang.", "");
            isList = fieldType.matches("(.*)List(.*)");

            try {
                if (!isList && !field.getName().equalsIgnoreCase("ID")) {
                    if (0 != query.length()) {
                        query.append(", ");
                    }
                    query.append(symbol).append("_").append(field.getName().toUpperCase());

                    String res = (String) field.get(t);
                    query.append(" = '")
                            .append(res).append("'");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        query.append(" where ").append(symbol).append("_ID = ").append(id);
        executeUpdate( update + query.toString());

        return t;
    }

    @Override
    public void delete(ID id) {
        String query = "DELETE FROM " + clearClassName + "S WHERE " + symbol + "_ID=" + id;
        executeUpdate(query);
    }

    private ResultSet executeUpdate(String query) {
        PreparedStatement statement;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private List<String> getObjectFields() {
        String fieldType;
        List<String> list = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fieldType = field.getAnnotatedType().getType().getTypeName()
                    .replaceFirst("java.lang.", "");

            isList = fieldType.matches("(.*)List(.*)");
            if (!isList) {
                list.add(symbol + "_" + field.getName().toUpperCase());
            }
        }
        return list;
    }

    private List<String> getObjectValues(T t) {
        List<String> list = new ArrayList<>();
        Class c = t.getClass();
        String value = null;
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);

            String fieldType = field.getAnnotatedType().getType().getTypeName()
                    .replaceFirst("java.lang.", "");
            isList = fieldType.matches("(.*)List(.*)");
            if (!isList) {
                if (field.getName().toUpperCase().equals("ID")) {
                    value = null;
                    list.add(value);
                } else {
                    try {
                        value = String.valueOf(field.get(t));
                        list.add(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return list;
    }

    // TODO      we save ID as Long
    private String getQueryForCreateTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ")
                .append(clearClassName).append("S").append(" ( ");


        String fieldType;

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fieldType = field.getAnnotatedType().getType().getTypeName()
                    .replaceFirst("java.lang.", "");
            isList = fieldType.matches("(.*)List(.*)");
            if (!isList) {
                if (field.getName().equalsIgnoreCase("ID")) {
                    sb.append(symbol).append("_").append("ID BIGINT PRIMARY KEY AUTO_INCREMENT, ");
                } else if (fieldType.equalsIgnoreCase("INT") ||
                        fieldType.equalsIgnoreCase("LONG")) {
                    sb.append(symbol).append("_").append("ID BIGINT PRIMARY KEY AUTO_INCREMENT, ");
                } else {
                    sb.append(symbol).append("_").append(field.getName()).append(" VARCHAR(255) NOT NULL, ");
                }
            }
        }
        sb.append(" );");
        return sb.toString();
    }

    // TODO      we always expect an ID as Long
    private List<T> parseResultSet(ResultSet rs) {
        List<T> list = new ArrayList<>();
        try {
            while (rs.next()) {
                T clz = (T) getGenericClass().newInstance();
                Class<?> c = clz.getClass();  //   Class<?> c ???

                Field field = null;
                String getStr;
                String fieldType;
                Field[] fields = c.getDeclaredFields();
                System.out.println(fields);

                for (int i = 0; fields.length > i; i++) {
                    field = fields[i];
                    field.setAccessible(true);
                    fieldType = field.getAnnotatedType().getType().getTypeName()
                            .replaceFirst("java.lang.", "");

                    isList = fieldType.matches("(.*)List(.*)");
                    if (!isList) {
                        if (fieldType.equalsIgnoreCase("Long")) {


                            field.set(clz, rs.getLong(symbol + "_ID"));
                        } else {
                            getStr = rs.getString(i + 1);
                            field.set(clz, getStr);
                        }
                    }
                }
                list.add(clz);
            }
        } catch (IllegalAccessException | SQLException | InstantiationException e) {
            e.printStackTrace();
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
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        return persistentClass;
    }
}
