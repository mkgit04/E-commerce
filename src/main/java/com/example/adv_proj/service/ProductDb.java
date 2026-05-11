package com.example.adv_proj.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.example.adv_proj.pojo.Product;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDb {

    private static final Logger LOGGER = Logger.getLogger(ProductDb.class.getName());

    public static List<Product> getProductList(String user) throws ClassNotFoundException, SQLException {

        try (Jedis jedis = new Jedis("localhost", 6379)) {
            // per user (better than global)
            String key = "rate:" + user;

            int count = 0;

            String value = jedis.get(key);

            if (value != null) {
                count = Integer.parseInt(value);
            }
            // block if too many requests
            if (count >= 5) {
                throw new RuntimeException("Too many requests");
            }

            // increase counter
            jedis.incr(key);

            // reset after 10 seconds windo
            jedis.expire(key, 10);


            Gson gson = new Gson();

            String cached = jedis.get("products");

            if (cached != null) {
                Type type = new TypeToken<ArrayList<Product>>() {
                }.getType();

                return gson.fromJson(cached, type);
            }


            ArrayList<Product> products = new ArrayList<>();

            try (Connection connection = DatabaseConnection.getConnection();

                 Statement statement = connection.createStatement();

                 ResultSet resultSet = statement.executeQuery("SELECT * FROM product_cards")) {

                while (resultSet.next()) {

                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("item");
                    float price = resultSet.getFloat("price");

                    products.add(new Product(id, price, name));
                }
            }


            String json = gson.toJson(products);
            jedis.setex("products", 60, json);


            return products;
        } catch (NumberFormatException | JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, "Failed to load products from cache or database", e);
            throw new RuntimeException("Unable to load products", e);
        }


    }

    public static boolean validateUser(String username, String password) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String passwordColumn = resolveUserPasswordColumn(connection);

            String storedPassword;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT " + passwordColumn + " FROM users WHERE username=?")) {
                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                    storedPassword = rs.getString(1);
                }
            }

            boolean verified = PasswordUtil.verifyPassword(password, storedPassword);
            if (verified && !PasswordUtil.isHashedFormat(storedPassword)) {
                try (PreparedStatement update = connection.prepareStatement(
                        "UPDATE users SET " + passwordColumn + "=? WHERE username=?")) {
                    update.setString(1, PasswordUtil.hashPassword(password));
                    update.setString(2, username);
                    update.executeUpdate();
                }
            }

            return verified;
        }
    }

    private static boolean hasColumn(Connection connection, String tableName, String columnName) throws SQLException {
        return connection.getMetaData().getColumns(connection.getCatalog(), null, tableName, columnName).next();
    }

    private static String resolveUserPasswordColumn(Connection connection) throws SQLException {
        if (hasColumn(connection, "users", "password")) {
            return "password";
        }
        if (hasColumn(connection, "users", "password_hash")) {
            return "password_hash";
        }
        throw new SQLException("No password column found in users table");
    }
}
