package com.example.adv_proj;

import java.sql.Timestamp;

public class Review {

    private final int id;
    private final int productId;
    private final String username;
    private final int rating;
    private final String title;
    private final String comment;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;

    public Review(int id,
                  int productId,
                  String username,
                  int rating,
                  String title,
                  String comment,
                  Timestamp createdAt,
                  Timestamp updatedAt) {
        this.id = id;
        this.productId = productId;
        this.username = username;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
}