package com.example.controllers;

import com.example.adv_proj.AppDao;
import com.example.adv_proj.pojo.Product;
import com.example.adv_proj.pojo.Review;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/reviews/view")
public class ViewReviewsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String productIdValue = request.getParameter("productId");

        if (productIdValue == null || productIdValue.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id is required");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdValue);
            Product product = AppDao.getProductById(productId);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }

            List<Review> reviews = AppDao.getReviewsByProduct(productId);
            request.setAttribute("product", product);
            request.setAttribute("reviews", reviews);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/view-reviews.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id must be numeric");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load reviews right now");
        }
    }
}