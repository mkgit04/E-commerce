package com.example.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.adv_proj.service.AppDao;

@WebServlet("/products/delete")
public class DeleteProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DeleteProductServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String user = (String) request.getAttribute("user");
            if (user == null || user.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign in required");
                return;
            }

            String idValue = request.getParameter("id");
            if (idValue == null || idValue.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id is required");
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
}
