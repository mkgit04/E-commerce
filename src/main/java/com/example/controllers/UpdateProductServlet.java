package com.example.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.adv_proj.pojo.Product;
import com.example.adv_proj.service.AppDao;
import com.example.adv_proj.service.ValidationUtil;

@WebServlet("/products/update")
public class UpdateProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UpdateProductServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String user = (String) request.getAttribute("user");
            if (user == null || user.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required");
                return;
            }

            String idValue = request.getParameter("id");
            String name = request.getParameter("name");
            String priceValue = request.getParameter("price");

            if (ValidationUtil.isNullOrBlank(idValue) || ValidationUtil.isNullOrBlank(name) || ValidationUtil.isNullOrBlank(priceValue)) {
                request.setAttribute("error", "Product id, name, and price are required");
                forwardToEdit(request, response, idValue);
                return;
            }

            if (!ValidationUtil.isValidProductId(idValue)) {
                request.setAttribute("error", ValidationUtil.INVALID_PRODUCT_ID);
                forwardToEdit(request, response, idValue);
                return;
            }

            if (!ValidationUtil.isValidProductName(name)) {
                request.setAttribute("error", ValidationUtil.INVALID_PRODUCT_NAME);
                forwardToEdit(request, response, idValue);
                return;
            }

            if (!ValidationUtil.isValidPrice(priceValue)) {
                request.setAttribute("error", ValidationUtil.INVALID_PRICE);
                forwardToEdit(request, response, idValue);
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

    private void forwardToEdit(HttpServletRequest request, HttpServletResponse response, String productIdValue) throws ServletException, IOException {
        try {
            if (ValidationUtil.isValidProductId(productIdValue)) {
                int productId = Integer.parseInt(productIdValue);
                Product product = AppDao.getProductById(productId);
                if (product != null) {
                    request.setAttribute("product", product);
                }
            }
            request.getRequestDispatcher("/edit-product.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load product for edit", e);
            throw new ServletException("Unable to load product", e);
        }
    }
}