package com.example.adv_proj.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserDao {

    private UserDao() {}

    private static boolean hasColumn(Connection connection, String tableName, String columnName) throws SQLException {
        return connection.getMetaData().getColumns(connection.getCatalog(), null, tableName, columnName).next();
    }

    private static String resolveUserPasswordColumn(Connection connection) throws SQLException {
        if (hasColumn(connection, "users", "password")) return "password";
        if (hasColumn(connection, "users", "password_hash")) return "password_hash";
        throw new SQLException("No password column found in users table");
    }

    private static String resolveRoleColumn(Connection connection) throws SQLException {
        if (hasColumn(connection, "users", "role")) return "role";
        if (hasColumn(connection, "users", "user_role")) return "user_role";
        return null;
    }

    public static boolean createUser(String username, String password) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String passwordColumn = resolveUserPasswordColumn(connection);
            String roleColumn = resolveRoleColumn(connection);
            String passwordHash = PasswordUtil.hashPassword(password);

            String sql;
            if (roleColumn == null) {
                sql = "INSERT INTO users (username, " + passwordColumn + ") VALUES (?, ?)";
            } else {
                sql = "INSERT INTO users (username, " + passwordColumn + ", " + roleColumn + ") VALUES (?, ?, ?)";
            }

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username.trim());
                ps.setString(2, passwordHash);
                if (roleColumn != null) ps.setString(3, "user");
                return ps.executeUpdate() > 0;
            }
        }
    }

    public static boolean deleteUser(String username) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE username=?")) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean isAdmin(String username) throws Exception {
        if (username == null || username.isBlank()) return false;
        if ("admin".equalsIgnoreCase(username)) return true;

        try (Connection connection = DatabaseConnection.getConnection()) {
            String roleColumn = resolveRoleColumn(connection);
            if (roleColumn == null) return false;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT 1 FROM users WHERE username=? AND " + roleColumn + "='admin'")) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }
}
