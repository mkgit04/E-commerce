package com.example.controllers;

import com.example.adv_proj.pojo.Review;
import com.example.adv_proj.service.ReviewDao;
import com.example.adv_proj.service.ValidationUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/reviews/edit")
public class EditReviewPageServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(EditReviewPageServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String user = (String) request.getAttribute("user");
        if (user == null || user.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required to edit reviews");
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
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only edit your own reviews");
                return;
            }

            request.setAttribute("review", review);
            request.setAttribute("currentUser", user);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/edit-review.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Review id must be numeric");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to open review edit page", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to open review edit page right now");
        }
    }
}
