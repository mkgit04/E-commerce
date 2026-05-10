package com.example.adv_proj.service;

import com.example.adv_proj.pojo.Product;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ProductDao {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private ProductDao() {}

    public static boolean addProduct(String name, float price) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String nameColumn = resolveProductNameColumn(connection);
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product_cards (" + nameColumn + ", price) VALUES (?, ?)")) {
                ps.setString(1, name);
                ps.setFloat(2, price);
                boolean created = ps.executeUpdate() > 0;
                if (created) invalidateProductCache();
                return created;
            }
        }
    }

    public static boolean deleteProduct(int productId) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM product_cards WHERE id=?")) {
            ps.setInt(1, productId);
            boolean deleted = ps.executeUpdate() > 0;
            if (deleted) invalidateProductCache();
            return deleted;
        }
    }

    public static boolean updateProduct(int productId, String name, float price) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String nameColumn = resolveProductNameColumn(connection);
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE product_cards SET " + nameColumn + "=?, price=? WHERE id=?")) {
                ps.setString(1, name);
                ps.setFloat(2, price);
                ps.setInt(3, productId);
                boolean updated = ps.executeUpdate() > 0;
                if (updated) invalidateProductCache();
                return updated;
            }
        }
    }

    public static Product getProductById(int productId) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM product_cards WHERE id=?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String nameColumn = resolveProductNameColumn(connection);
                return new Product(rs.getInt("id"), rs.getFloat("price"), rs.getString(nameColumn));
            }
        }
    }

    public static void invalidateProductCache() {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            jedis.del("products");
        } catch (Exception e) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.WARNING, "Failed to invalidate product cache", e);
        }
    }

    private static boolean hasColumn(Connection connection, String tableName, String columnName) throws Exception {
        return connection.getMetaData().getColumns(connection.getCatalog(), null, tableName, columnName).next();
    }

    private static String resolveProductNameColumn(Connection connection) throws Exception {
        if (hasColumn(connection, "product_cards", "item")) return "item";
        if (hasColumn(connection, "product_cards", "name")) return "name";
        throw new Exception("No product name column found in product_cards table");
    }
}
