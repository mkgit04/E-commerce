package com.example.adv_proj;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/products/add")
public class AddProductServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String user = (String) request.getAttribute("user");
            if (!AppDao.isAdmin(user)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
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
            response.sendRedirect("ProductsMain");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Price must be numeric");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
