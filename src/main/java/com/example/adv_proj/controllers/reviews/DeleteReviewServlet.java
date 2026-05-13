package com.example.adv_proj.controllers.reviews;

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

@WebServlet("/reviews/delete")
public class DeleteReviewServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DeleteReviewServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String user = (String) request.getAttribute("user");
        if (user == null || user.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required");
            return;
        }

        String reviewIdValue = request.getParameter("reviewId");

        if (ValidationUtil.isNullOrBlank(reviewIdValue)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Review id is required");
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
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only delete your own reviews");
                return;
            }

            int productId = review.getProductId();
            boolean deleted = ReviewDao.deleteReview(reviewId, user);

            if (!deleted) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete review");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/reviews/view?productId=" + productId + "&review=deleted");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Review id must be numeric");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete review", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to delete review right now");
        }
    }
}
