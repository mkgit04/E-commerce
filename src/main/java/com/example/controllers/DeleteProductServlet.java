package com.example.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.example.adv_proj.AppDao;

@WebServlet("/products/delete")
public class DeleteProductServlet extends HttpServlet {

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
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to delete product right now");
        }
    }
}
