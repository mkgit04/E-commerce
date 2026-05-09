package com.example.controllers;

import com.example.adv_proj.AppDao;
import com.example.adv_proj.pojo.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/product/edit")
public class EditProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(EditProductServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idValue = request.getParameter("id");

        if (idValue == null || idValue.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id is required");
            return;
        }

        try {
            Product product = AppDao.getProductById(Integer.parseInt(idValue));
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }

            request.setAttribute("product", product);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/edit-product.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id must be numeric");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load product for edit", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load product right now");
        }
    }
}
