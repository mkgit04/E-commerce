package com.example.adv_proj;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if (username == null || username.isBlank() || password == null || password.isBlank()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username and password are required");
        return;
    }

    try {
        if (AppDao.createUser(username, password)) {
            response.sendRedirect("login.jsp");
            return;
        }
        response.sendError(HttpServletResponse.SC_CONFLICT, "Unable to create account");
    } catch (Exception e) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
}
