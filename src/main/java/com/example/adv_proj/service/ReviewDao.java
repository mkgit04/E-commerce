package com.example.adv_proj.service;

import com.example.adv_proj.pojo.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class ReviewDao {
    private ReviewDao() {}

    public static boolean addReview(int productId, String username, int rating, String title, String comment) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            try (PreparedStatement check = connection.prepareStatement(
                    "SELECT 1 FROM reviews WHERE product_id=? AND username=?")) {
                check.setInt(1, productId);
                check.setString(2, username);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) return false;
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
        try (Connection connection = DatabaseConnection.getConnection();
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
}
