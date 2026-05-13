package com.example.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.adv_proj.pojo.Review;
import com.example.adv_proj.service.ReviewDao;
import com.example.adv_proj.service.ValidationUtil;

@WebServlet("/reviews/update")
public class UpdateReviewServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UpdateReviewServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String user = (String) request.getAttribute("user");
        if (user == null || user.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required");
            return;
        }

        String reviewIdValue = request.getParameter("reviewId");
        String ratingValue = request.getParameter("rating");
        String title = request.getParameter("title");
        String comment = request.getParameter("comment");

        if (ValidationUtil.isNullOrBlank(reviewIdValue) || ValidationUtil.isNullOrBlank(ratingValue)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Review and rating are required");
            return;
        }

        if (!ValidationUtil.isValidRating(ratingValue)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ValidationUtil.INVALID_RATING);
            return;
        }

        try {
            int reviewId = Integer.parseInt(reviewIdValue);
            Review review = ReviewDao.getReviewById(reviewId);

            if (review == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
                return;
            }

            if (!review.getUsername().equals(user)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only update your own reviews");
                return;
            }

            int rating = Integer.parseInt(ratingValue);
            boolean updated = ReviewDao.updateReview(reviewId, user, rating,
                    title == null ? null : title.trim(),
                    comment == null ? null : comment.trim());

            if (!updated) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update review");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/reviews/view?productId=" + review.getProductId() + "&review=updated");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid review or rating value");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update review", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update review right now");
        }
    }
}
