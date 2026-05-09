package com.example.adv_proj.pojo;

import java.sql.Timestamp;

public class Review {

    private int id;
    private int productId;
    private String username;
    private int rating;
    private String title;
    private String comment;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Review() {
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}