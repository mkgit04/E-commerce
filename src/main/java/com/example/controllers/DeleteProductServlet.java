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
import com.example.adv_proj.service.ProductDb;
import com.example.adv_proj.service.ValidationUtil;

@WebServlet("/products/delete")
public class DeleteProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DeleteProductServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String user = (String) request.getAttribute("user");
            if (user == null || user.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required");
                return;
            }

            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (!Boolean.TRUE.equals(isAdmin)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
                return;
            }

            String idValue = request.getParameter("id");
            if (ValidationUtil.isNullOrBlank(idValue)) {
                request.setAttribute("error", "Product id is required");
                loadProductsAndForward(request, response);
                return;
            }

            if (!ValidationUtil.isValidProductId(idValue)) {
                request.setAttribute("error", ValidationUtil.INVALID_PRODUCT_ID);
                loadProductsAndForward(request, response);
                return;
            }

            AppDao.deleteProduct(Integer.parseInt(idValue));
            response.sendRedirect(request.getContextPath() + "/ProductsMain");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id must be numeric");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete product", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to delete product right now");
        }
    }

    private void loadProductsAndForward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String user = (String) request.getAttribute("user");
            java.util.List<Product> data = ProductDb.getProductList(user);
            request.setAttribute("data", data);
            request.getRequestDispatcher("/display-products.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load products", e);
            throw new ServletException("Unable to load products", e);
        }
    }
}
