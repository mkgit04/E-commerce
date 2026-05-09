package com.example.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.adv_proj.service.AppDao;

@WebServlet("/products/add")
public class AddProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddProductServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String user = (String) request.getAttribute("user");
            if (user == null || user.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required");
                return;
            }

            String name = request.getParameter("name");
            String priceValue = request.getParameter("price");
            if (name == null || name.isBlank() || priceValue == null || priceValue.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name and price are required");
                return;
            }

            float price = Float.parseFloat(priceValue);
            AppDao.addProduct(name.trim(), price);
            response.sendRedirect(request.getContextPath() + "/ProductsMain");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Price must be numeric");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to add product", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to add product right now");
        }
    }
}
