package com.example.adv_proj;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ProductsMain")
public class ProductsMain extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String user = (String) request.getAttribute("user");
            List<Product> data = ProductDb.getProductList(user);
            request.setAttribute("data", data);

            RequestDispatcher dispatcher = request.getRequestDispatcher("display-products.jsp");
            dispatcher.forward(request, response);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Database driver not available", e);
        } catch (SQLException e) {
            throw new ServletException("Unable to load products", e);
        }
    }
}
