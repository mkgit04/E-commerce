package com.example.adv_proj;

import com.example.adv_proj.pojo.Product;
import com.example.adv_proj.pojo.Review;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppDao {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/products?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static boolean hasColumn(Connection connection, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableName, columnName)) {
            return columns.next();
        }
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

    private static String resolveUserEmailColumn(Connection connection) throws SQLException {
        if (hasColumn(connection, "users", "email")) {
            return "email";
        }
        return null;
    }

    private static String resolveRoleColumn(Connection connection) throws SQLException {
        if (hasColumn(connection, "users", "role")) {
            return "role";
        }
        if (hasColumn(connection, "users", "user_role")) {
            return "user_role";
        }
        return null;
    }

    private static String resolveProductNameColumn(Connection connection) throws SQLException {
        if (hasColumn(connection, "product_cards", "item")) {
            return "item";
        }
        if (hasColumn(connection, "product_cards", "name")) {
            return "name";
        }
        throw new SQLException("No product name column found in product_cards table");
    }

    public static boolean createUser(String username, String password) throws Exception {
        try (Connection connection = getConnection()) {
            String passwordColumn = resolveUserPasswordColumn(connection);
            
            String sql = "INSERT INTO users (username, " + passwordColumn + ") VALUES (?, ?)";
            
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username.trim());
                ps.setString(2, password);
                return ps.executeUpdate() > 0;
            }
        }
    }

    public static boolean deleteUser(String username) throws Exception {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE username=?")) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean isAdmin(String username) throws Exception {
        if (username == null || username.isBlank()) {
            return false;
        }
        if ("admin".equalsIgnoreCase(username)) {
            return true;
        }

        try (Connection connection = getConnection()) {
            String roleColumn = resolveRoleColumn(connection);
            if (roleColumn == null) {
                return false;
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT 1 FROM users WHERE username=? AND " + roleColumn + "='admin'")) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    public static boolean addProduct(String name, float price) throws Exception {
        try (Connection connection = getConnection()) {
            String nameColumn = resolveProductNameColumn(connection);
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product_cards (" + nameColumn + ", price) VALUES (?, ?)")) {
                ps.setString(1, name);
                ps.setFloat(2, price);
                boolean created = ps.executeUpdate() > 0;
                if (created) {
                    invalidateProductCache();
                }
                return created;
            }
        }
    }

    public static boolean deleteProduct(int productId) throws Exception {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM product_cards WHERE id=?")) {
            ps.setInt(1, productId);
            boolean deleted = ps.executeUpdate() > 0;
            if (deleted) {
                invalidateProductCache();
            }
            return deleted;
        }
    }

    public static boolean updateProduct(int productId, String name, float price) throws Exception {
        try (Connection connection = getConnection()) {
            String nameColumn = resolveProductNameColumn(connection);
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE product_cards SET " + nameColumn + "=?, price=? WHERE id=?")) {
                ps.setString(1, name);
                ps.setFloat(2, price);
                ps.setInt(3, productId);
                boolean updated = ps.executeUpdate() > 0;
                if (updated) {
                    invalidateProductCache();
                }
                return updated;
            }
        }
    }

    public static Product getProductById(int productId) throws Exception {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM product_cards WHERE id=?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String nameColumn = resolveProductNameColumn(connection);
                return new Product(rs.getInt("id"), rs.getFloat("price"), rs.getString(nameColumn));
            }
        }
    }

    public static boolean addReview(int productId, String username, int rating, String title, String comment) throws Exception {
        try (Connection connection = getConnection()) {
            try (PreparedStatement check = connection.prepareStatement(
                    "SELECT 1 FROM reviews WHERE product_id=? AND username=?")) {
                check.setInt(1, productId);
                check.setString(2, username);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }

            try (PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO reviews (product_id, username, rating, title, comment) VALUES (?, ?, ?, ?, ?)")) {
                insert.setInt(1, productId);
                insert.setString(2, username);
                insert.setInt(3, rating);
                insert.setString(4, title);
                insert.setString(5, comment);
                return insert.executeUpdate() > 0;
            }
        }
    }

    public static List<Review> getReviewsByProduct(int productId) throws Exception {
        List<Review> reviews = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT id, product_id, username, rating, title, comment, created_at, updated_at " +
                             "FROM reviews WHERE product_id=? ORDER BY created_at DESC")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(new Review(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getString("username"),
                            rs.getInt("rating"),
                            rs.getString("title"),
                            rs.getString("comment"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    ));
                }
            }
        }
        return reviews;
    }

    public static void createSession(String sessionId, String username, int ttlSeconds) {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            jedis.setex("session:" + sessionId, ttlSeconds, username);
        }
    }

    public static String getSessionUser(String sessionId) {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            return jedis.get("session:" + sessionId);
        }
    }

    public static void deleteSession(String sessionId) {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            jedis.del("session:" + sessionId);
        }
    }

    public static void invalidateProductCache() {
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            jedis.del("products");
        }
    }
}
