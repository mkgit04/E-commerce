package com.example.adv_proj.controllers.reviews;

import com.example.adv_proj.pojo.Product;
import com.example.adv_proj.service.ProductDao;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/reviews/new")
public class SubmitReviewPageServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SubmitReviewPageServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String user = (String) request.getAttribute("user");
        if (user == null || user.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required to submit reviews");
            return;
        }

        String productIdValue = request.getParameter("productId");

        if (productIdValue == null || productIdValue.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id is required");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdValue);
            Product product = ProductDao.getProductById(productId);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("currentUser", user);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/submit-review.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id must be numeric");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to open review page", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to open review page right now");
        }
    }
}