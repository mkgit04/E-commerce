package com.example.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.example.adv_proj.service.AppDao;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if (username == null || username.isBlank() || password == null || password.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/invalid-user.jsp?reason=missingFields");
        return;
    }

    try {
        if (AppDao.createUser(username, password)) {
                response.sendRedirect(request.getContextPath() + "/login.jsp?registered=true");
            return;
        }
            response.sendRedirect(request.getContextPath() + "/invalid-user.jsp?reason=accountExists");
    } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/invalid-user.jsp?reason=registrationError");
    }
}
}
