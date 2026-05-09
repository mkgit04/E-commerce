package com.example.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.example.adv_proj.AppDao;

@WebServlet("/reviews/add")
public class AddReviewServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = (String) request.getAttribute("user");
        if (user == null || user.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required");
            return;
        }

        String productIdValue = request.getParameter("productId");
        String ratingValue = request.getParameter("rating");
        String title = request.getParameter("title");
        String comment = request.getParameter("comment");

        if (productIdValue == null || productIdValue.isBlank() || ratingValue == null || ratingValue.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product and rating are required");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdValue);
            int rating = Integer.parseInt(ratingValue);

            if (rating < 1 || rating > 5) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Rating must be between 1 and 5");
                return;
            }

            boolean created = AppDao.addReview(productId, user, rating,
                    title == null ? null : title.trim(),
                    comment == null ? null : comment.trim());

            if (!created) {
                response.sendRedirect(request.getContextPath() + "/reviews/new?productId=" + productId + "&review=exists");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/reviews/new?productId=" + productId + "&review=added");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product or rating value");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}