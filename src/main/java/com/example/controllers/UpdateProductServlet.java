package com.example.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.adv_proj.service.AppDao;

@WebServlet("/products/update")
public class UpdateProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UpdateProductServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String user = (String) request.getAttribute("user");
            if (user == null || user.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required");
                return;
            }

            String idValue = request.getParameter("id");
            String name = request.getParameter("name");
            String priceValue = request.getParameter("price");

            if (idValue == null || idValue.isBlank() || name == null || name.isBlank() || priceValue == null || priceValue.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id, name, and price are required");
                return;
            }

            int productId = Integer.parseInt(idValue);
            float price = Float.parseFloat(priceValue);
            AppDao.updateProduct(productId, name.trim(), price);
            response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id and price must be numeric");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update product", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update product right now");
        }
    }
}