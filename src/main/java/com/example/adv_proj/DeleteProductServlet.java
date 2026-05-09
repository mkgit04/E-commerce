package com.example.adv_proj;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/products/delete")
public class DeleteProductServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String user = (String) request.getAttribute("user");
            if (!AppDao.isAdmin(user)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
                return;
            }

            String idValue = request.getParameter("id");
            if (idValue == null || idValue.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id is required");
                return;
            }

            AppDao.deleteProduct(Integer.parseInt(idValue));
            response.sendRedirect("ProductsMain");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id must be numeric");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
