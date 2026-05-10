package com.example.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.adv_proj.service.ProductDb;
import com.example.adv_proj.service.SessionDao;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            if (ProductDb.validateUser(username, password)) {
                String sessionId = UUID.randomUUID().toString();
                SessionDao.createSession(sessionId, username, 3600);

                Cookie sessionCookie = new Cookie("SESSION_ID", sessionId);
                sessionCookie.setPath("/");
                sessionCookie.setHttpOnly(true);
                sessionCookie.setMaxAge(3600);
                response.addCookie(sessionCookie);

                String token = JWT.create()
                        .withClaim("user", username)
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 3_600_000L))
                        .sign(Algorithm.HMAC256("secret"));

                Cookie tokenCookie = new Cookie("AUTH_TOKEN", token);
                tokenCookie.setPath("/");
                tokenCookie.setHttpOnly(true);
                tokenCookie.setMaxAge(3600);
                response.addCookie(tokenCookie);

                response.sendRedirect(request.getContextPath() + "/ProductsMain");
            } else {
                response.sendRedirect(request.getContextPath() + "/invalid-user.jsp?reason=invalidCredentials");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to sign in user", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to sign in right now");
        }
    }
}
