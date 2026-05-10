package com.example.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.example.adv_proj.service.UserDao;
import com.example.adv_proj.service.ValidationUtil;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (ValidationUtil.isNullOrBlank(username) || ValidationUtil.isNullOrBlank(password)) {
            request.setAttribute("error", "Please fill in both username and password.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidUsername(username)) {
            request.setAttribute("error", ValidationUtil.INVALID_USERNAME);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            request.setAttribute("error", ValidationUtil.INVALID_PASSWORD);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            if (UserDao.createUser(username, password)) {
                response.sendRedirect(request.getContextPath() + "/login.jsp?registered=true");
                return;
            }

        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                request.setAttribute("error", "That account already exists.");
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
            }
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
